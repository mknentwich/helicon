package at.markusnentwich.helicon.services

import at.markusnentwich.helicon.dto.ScoreOrderDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class OrderServiceImpl:OrderService {
    override fun getAll(jwt: String): ResponseEntity<Iterable<ScoreOrderDto>> {
        TODO("Not yet implemented")
    }

    override fun getOrderById(id: UUID, jwt: String): ResponseEntity<ScoreOrderDto> {
        TODO("Not yet implemented")
    }

    override fun order(order: ScoreOrderDto, jwt: String): ResponseEntity<ScoreOrderDto> {
        TODO("Not yet implemented")
    }

    override fun confirm(id: UUID, jwt: String): ResponseEntity<ScoreOrderDto> {
        TODO("Not yet implemented")
    }
}