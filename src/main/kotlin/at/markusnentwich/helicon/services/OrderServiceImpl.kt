package at.markusnentwich.helicon.services

import at.markusnentwich.helicon.controller.BadPayloadException
import at.markusnentwich.helicon.controller.NotFoundException
import at.markusnentwich.helicon.controller.OrderController
import at.markusnentwich.helicon.dto.ScoreOrderDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController(ORDER_SERVICE)
class OrderServiceImpl(
    @Autowired val orderController: OrderController
) : OrderService {
    val logger = LoggerFactory.getLogger(OrderServiceImpl::class.java)
    override fun getAll(jwt: String): ResponseEntity<Iterable<ScoreOrderDto>> {
        callLog("getAll")
        return ResponseEntity.ok(orderController.getAll(jwt))
    }

    override fun getOrderById(id: UUID, jwt: String): ResponseEntity<ScoreOrderDto> {
        callLog("getOrderById")
        val status = try {
            return ResponseEntity.ok(orderController.getOrderById(id, jwt))
        } catch (e: NotFoundException) {
            HttpStatus.NOT_FOUND
        }
        return ResponseEntity.status(status).build()
    }

    override fun order(order: ScoreOrderDto, jwt: String): ResponseEntity<ScoreOrderDto> {
        callLog("order")
        val status = try {
            return ResponseEntity.ok(orderController.order(order, jwt))
        } catch (e: BadPayloadException) {
            HttpStatus.BAD_REQUEST
        }
        return ResponseEntity.status(status).build()
    }

    override fun confirm(id: UUID, jwt: String): ResponseEntity<ScoreOrderDto> {
        callLog("confirm")
        val status = try {
            return ResponseEntity.ok(orderController.confirm(id, jwt))
        } catch (e: NotFoundException) {
            HttpStatus.NOT_FOUND
        }
        return ResponseEntity.status(status).build()
    }

    override fun bill(id: UUID, jwt: String): ResponseEntity<Array<Byte>> {
        TODO("Not yet implemented")
    }

    fun callLog(method: String) {
        logger.trace("called {}", method)
    }
}
