package at.markusnentwich.helicon.controller

import at.markusnentwich.helicon.configuration.HeliconConfigurationProperties
import at.markusnentwich.helicon.dto.*
import at.markusnentwich.helicon.entities.AddressEntity
import at.markusnentwich.helicon.entities.IdentityEntity
import at.markusnentwich.helicon.entities.OrderEntity
import at.markusnentwich.helicon.entities.OrderScoreEntity
import at.markusnentwich.helicon.mail.OrderMailService
import at.markusnentwich.helicon.repositories.AddressRepository
import at.markusnentwich.helicon.repositories.IdentityRepository
import at.markusnentwich.helicon.repositories.OrderRepository
import at.markusnentwich.helicon.repositories.OrderScoreRepository
import at.markusnentwich.helicon.repositories.ScoreRepository
import org.modelmapper.ModelMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import java.time.LocalDateTime
import java.util.*
import javax.transaction.Transactional

@Controller
class OrderControllerImpl(
    @Autowired val config: HeliconConfigurationProperties,
    @Autowired val orderRepo: OrderRepository,
    @Autowired val orderScoreRepository: OrderScoreRepository,
    @Autowired val scoreRepository: ScoreRepository,
    @Autowired val identityRepository: IdentityRepository,
    @Autowired val addressRepository: AddressRepository,
    @Autowired val mapper: ModelMapper,
    @Autowired val orderMailService: OrderMailService
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
        dto.items = items
        return dto
    }

    @Transactional
    override fun order(order: ScoreOrderDto, jwt: String): ScoreOrderDto {
        var orderEntity = mapper.map(order, OrderEntity::class.java)
        var deliveryAddress = if (order.deliveryAddress == null) {
            null
        } else {
            mapper.map(order.deliveryAddress, AddressEntity::class.java)
        }
        var identityAddress = mapper.map(order.identity.address, AddressEntity::class.java)
        var identity = mapper.map(order.identity, IdentityEntity::class.java)
        if (order.items.isEmpty()) {
            logger.error("received order without items")
            throw BadPayloadException()
        }
        if (order.items.any {
            it.quantity == null || it.quantity!! < 1 || it.id == null || !scoreRepository.existsById(it.id!!)
        }
        ) {
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
        orderEntity.receivedOn = LocalDateTime.now()
        val orderLinks = order.items.map {
            OrderScoreEntity(
                amount = it.quantity!!,
                score = scoreRepository.findById(it.id!!).get(),
                order = orderEntity
            )
        }.toMutableList()
        orderScoreRepository.saveAll(orderLinks)
        val orderDto = mapper.map(orderEntity, ScoreOrderDto::class.java)
        orderDto.total = orderEntity.total()
        orderDto.items = orderLinks.map {
            val dt = mapper.map(scoreRepository.findById(it.score.id!!), ScoreProductDto::class.java)
            dt.quantity = it.amount
            dt
        }.toMutableSet()
        return orderDto
    }

    override fun confirm(id: UUID, jwt: String): ScoreOrderDto {
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
        val dto = mapper.map(orderRepo.save(entity), ScoreOrderDto::class.java)
        dto.total = entity.total()
        dto.items = entity.items.map {
            it.score.category.scores = null
            val dt = mapper.map(it.score, ScoreProductDto::class.java)
            dt.quantity = it.amount
            dt
        }.toMutableSet()
        // TODO dto mapping
        // TODO email notifications
        orderMailService.notifyOwner(entity)
        orderMailService.notifyCustomer(entity)
        return dto
    }
}
