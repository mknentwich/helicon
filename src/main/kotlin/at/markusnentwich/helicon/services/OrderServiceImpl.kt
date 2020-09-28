package at.markusnentwich.helicon.services

import at.markusnentwich.helicon.dto.ScoreOrderDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class OrderServiceImpl:OrderService {
    override fun getAll() {
        TODO("Not yet implemented")
    }

    override fun getOrderById() {
        TODO("Not yet implemented")
    }

    override fun order(order: ScoreOrderDto): ResponseEntity<ScoreOrderDto> {
        TODO("Not yet implemented")
    }

    override fun confirm(id: UUID): ResponseEntity<ScoreOrderDto> {
        TODO("Not yet implemented")
    }
}