package at.markusnentwich.helicon.controller

import at.markusnentwich.helicon.configuration.HeliconConfigurationProperties
import at.markusnentwich.helicon.dto.*
import at.markusnentwich.helicon.entities.AddressEntity
import at.markusnentwich.helicon.entities.IdentityEntity
import at.markusnentwich.helicon.entities.OrderEntity
import at.markusnentwich.helicon.entities.OrderScoreEntity
import at.markusnentwich.helicon.mail.OrderMailService
import at.markusnentwich.helicon.repositories.*
import org.modelmapper.ModelMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

@Controller
class OrderControllerImpl(
    @Autowired val config: HeliconConfigurationProperties,
    @Autowired val orderRepo: OrderRepository,
    @Autowired val orderScoreRepository: OrderScoreRepository,
    @Autowired val scoreRepository: ScoreRepository,
    @Autowired val identityRepository: IdentityRepository,
    @Autowired val addressRepository: AddressRepository,
    @Autowired val mapper: ModelMapper,
    @Autowired val orderMailService: OrderMailService,
    @Autowired val stateRepository: StateRepository,
    @Autowired val billConverter: BillConverter,
) : OrderController {
    private val logger = LoggerFactory.getLogger(OrderControllerImpl::class.java)

    override fun getAll(jwt: String): List<ScoreOrderDto> {
        TODO("Not yet implemented")
    }

    override fun getOrderById(id: UUID, jwt: String): ScoreOrderDto {
        // TODO check permissions
        if (!orderRepo.existsById(id)) {
            logger.error("order with id {} does not exist", id)
            throw NotFoundException()
        }
        val entity = orderRepo.findById(id).get()
        val dto = mapper.map(entity, ScoreOrderDto::class.java)
        dto.deliveryAddress =
            if (entity.deliveryAddress == null) null else mapper.map(entity.deliveryAddress, AddressDto::class.java)
        dto.identity = mapper.map(entity.identity, IdentityDto::class.java)
        dto.customer = if (entity.customer == null) null else mapper.map(entity.customer, AccountDto::class.java)
        if (dto.customer != null) {
            dto.customer!!.identity = mapper.map(entity.customer?.identity, IdentityDto::class.java)
        }
        val items = entity.items.map { mapper.map(it, ScoreProductDto::class.java) }.toMutableSet()
        dto.orderedItems = items
        dto.shippingCosts = entity.shipping
        dto.taxRate = entity.taxRate?.toDouble()
        dto.taxes = entity.taxes()
        dto.totalBeforeTaxes = entity.beforeTaxes()
        return dto
    }

    override fun order(order: ScoreOrderDto): ScoreOrderDto {
        var orderEntity = mapper.map(order, OrderEntity::class.java)
        var deliveryAddress: AddressEntity? = null
        if (order.deliveryAddress != null) {
            deliveryAddress = mapper.map(order.deliveryAddress, AddressEntity::class.java)
            deliveryAddress.state =
                stateRepository.findByIdOrNull(order.deliveryAddress?.stateId!!) ?: throw BadPayloadException()
        }
        var identityAddress = mapper.map(order.identity.address, AddressEntity::class.java)
        identityAddress.state =
            stateRepository.findByIdOrNull(order.identity.address.stateId!!) ?: throw BadPayloadException()
        var identity = mapper.map(order.identity, IdentityEntity::class.java)
        if (order.items.isEmpty()) {
            logger.error("received order without items")
            throw BadPayloadException()
        }
        if (order.items.any { it.quantity < 1 || !scoreRepository.existsById(it.id) }) {
            logger.error("received order with invalid items (null ids/null quantity/too low quantity/non existing scores)")
            throw BadPayloadException()
        }
        identityAddress = addressRepository.save(identityAddress)
        if (deliveryAddress != null) {
            deliveryAddress = addressRepository.save(deliveryAddress)
        }
        identity.address = identityAddress
        identity = identityRepository.save(identity)
        orderEntity.identity = identity
        orderEntity.deliveryAddress = deliveryAddress
        orderEntity = orderRepo.save(orderEntity)
        orderEntity.taxRate = identityAddress.state.bookTaxes
        orderEntity.shipping = identityAddress.state.zone.shipping
        orderEntity.receivedOn = LocalDateTime.now()
        val orderLinks: Iterable<OrderScoreEntity> = order.items.map {
            OrderScoreEntity(
                amount = it.quantity,
                score = scoreRepository.findById(it.id).get(),
                order = orderEntity
            )
        }.toMutableList()
        orderScoreRepository.saveAll(orderLinks)
        orderRepo.refresh(orderEntity)
        val orderDto = mapper.map(orderEntity, ScoreOrderDto::class.java)
        orderDto.total = orderEntity.total()
        orderDto.orderedItems = orderEntity.items.map {
            it.score.category.scores = null
            val dt = mapper.map(it.score, ScoreProductDto::class.java)
            dt.quantity = it.amount
            dt
        }.toMutableSet()
        orderDto.shippingCosts = orderEntity.shipping
        orderDto.taxRate = orderEntity.taxRate?.toDouble()
        orderDto.taxes = orderEntity.taxes()
        orderDto.totalBeforeTaxes = orderEntity.beforeTaxes()
        return orderDto
    }

    override fun confirm(id: UUID): ScoreOrderDto {
        // TODO check permissions
        if (!config.order.enable) {
            logger.error("tried to confirm order {}, but confirmation is not enabled", id)
            throw NotFoundException()
        }
        if (!orderRepo.existsById(id)) {
            logger.error("order with id {} does not exist", id)
            throw NotFoundException()
        }
        val entity = orderRepo.findById(id).get()
        if (entity.confirmed != null) {
            logger.error("order with id {} was already confirmed at {}", id, entity.confirmed)
            throw BadPayloadException()
        }
        entity.confirmed = LocalDateTime.now()
        val evaluatedEntity = orderRepo.save(entity)
        orderRepo.refresh(evaluatedEntity)
        val dto = mapper.map(evaluatedEntity, ScoreOrderDto::class.java)
        dto.total = evaluatedEntity.total()
        dto.billingNumber = evaluatedEntity.billingNumber
        dto.orderedItems = evaluatedEntity.items.map {
            it.score.category.scores = null
            val dt = mapper.map(it.score, ScoreProductDto::class.java)
            dt.quantity = it.amount
            dt
        }.toMutableSet()
        dto.shippingCosts = evaluatedEntity.shipping
        dto.taxRate = evaluatedEntity.taxRate?.toDouble()
        dto.taxes = evaluatedEntity.taxes()
        dto.totalBeforeTaxes = evaluatedEntity.beforeTaxes()
        if (config.mail.notification.ownerOnOrder) {
            orderMailService.notifyOwner(evaluatedEntity)
        } else {
            logger.warn("Received an order but owner notifications are disabled")
        }
        if (config.mail.notification.customerOnOrder) {
            orderMailService.notifyCustomer(evaluatedEntity)
        } else {
            logger.warn("Received an order but customer notifications are disabled")
        }
        return dto
    }

    @Transactional
    override fun billCollection(from: LocalDate, to: LocalDate, confirmed: Boolean, jwt: String): ByteArray {
        val orders = orderRepo.findOrdersInRange(from.atStartOfDay(), to.atStartOfDay(), confirmed)
        val byteOut = ByteArrayOutputStream()
        val zipStream = ZipOutputStream(byteOut)
        orders.forEach {
            val entry = ZipEntry("rechnung-${it.billingNumber ?: UUID.randomUUID()}.pdf")
            zipStream.putNextEntry(entry)

            val pdfInput = billConverter.createBill(it)
            zipStream.write(pdfInput.readAllBytes())
            zipStream.closeEntry()
        }
        zipStream.close()
        return byteOut.toByteArray()
    }
}
