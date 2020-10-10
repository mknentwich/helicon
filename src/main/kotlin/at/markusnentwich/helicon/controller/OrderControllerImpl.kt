package at.markusnentwich.helicon.controller

import at.markusnentwich.helicon.configuration.HeliconConfigurationProperties
import at.markusnentwich.helicon.dto.*
import at.markusnentwich.helicon.repositories.OrderRepository
import org.modelmapper.ModelMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import java.time.LocalDateTime
import java.util.*

@Controller
class OrderControllerImpl(
    @Autowired val config: HeliconConfigurationProperties,
    @Autowired val orderRepo: OrderRepository,
    @Autowired val mapper: ModelMapper
) : OrderController {
    val logger = LoggerFactory.getLogger(OrderControllerImpl::class.java)

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
        dto.deliveryAddress = if (entity.deliveryAddress == null) null else mapper.map(entity.deliveryAddress, AddressDto::class.java)
        dto.identity = mapper.map(entity.identity, IdentityDto::class.java)
        dto.customer = if (entity.customer == null) null else mapper.map(entity.customer, AccountDto::class.java)
        if (dto.customer != null) {
            dto.customer!!.identity = mapper.map(entity.customer?.identity, IdentityDto::class.java)
        }
        val items = mutableListOf<ScoreProductDto>()
        entity.items.forEach { items.add(mapper.map(it, ScoreProductDto::class.java)) }
        dto.items = items
        return dto
    }

    override fun order(order: ScoreOrderDto, jwt: String): ScoreOrderDto {
        TODO("Not yet implemented")
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
        // TODO dto mapping
        // TODO email notifications
        return dto
    }
}
