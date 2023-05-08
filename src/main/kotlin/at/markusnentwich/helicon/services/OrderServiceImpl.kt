package at.markusnentwich.helicon.services

import at.markusnentwich.helicon.controller.BadPayloadException
import at.markusnentwich.helicon.controller.NotFoundException
import at.markusnentwich.helicon.controller.OrderController
import at.markusnentwich.helicon.dto.ScoreOrderDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.io.FileNotFoundException
import java.time.LocalDate
import java.util.*

@RestController(ORDER_SERVICE)
class OrderServiceImpl(
    @Autowired val orderController: OrderController
) : OrderService {
    private val logger = LoggerFactory.getLogger(OrderServiceImpl::class.java)
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

    override fun order(order: ScoreOrderDto, jwt: String?): ResponseEntity<ScoreOrderDto> {
        callLog("order")
        val status = try {
            return ResponseEntity.ok(orderController.order(order))
        } catch (e: BadPayloadException) {
            HttpStatus.BAD_REQUEST
        }
        return ResponseEntity.status(status).build()
    }

    override fun confirm(id: UUID, jwt: String?): ResponseEntity<ScoreOrderDto> {
        callLog("confirm")
        val status = try {
            return ResponseEntity.ok(orderController.confirm(id))
        } catch (e: NotFoundException) {
            HttpStatus.NOT_FOUND
        }
        return ResponseEntity.status(status).build()
    }

    override fun bill(id: UUID, jwt: String): ResponseEntity<Array<Byte>> {
        TODO("Not yet implemented")
    }

    override fun billCollection(
        from: LocalDate,
        to: LocalDate,
        confirmed: Boolean,
        jwt: String
    ): ResponseEntity<Resource> {
        callLog("billCollection")
        val status = try {
            val zip = orderController.billCollection(from, to, confirmed, jwt)
            return ResponseEntity.ok()
                .header("Content-Disposition", "attachment;filename=\"collection.zip\"")
                .contentLength(zip.size.toLong())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(ByteArrayResource(zip))
        } catch (e: FileNotFoundException) {
            HttpStatus.NOT_FOUND
        } catch (e: Exception) {
            HttpStatus.INTERNAL_SERVER_ERROR
        }
        return ResponseEntity.status(status).build()
    }

    fun callLog(method: String) {
        logger.trace("called {}", method)
    }
}
