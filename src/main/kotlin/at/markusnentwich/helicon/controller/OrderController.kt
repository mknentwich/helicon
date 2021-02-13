package at.markusnentwich.helicon.controller

import at.markusnentwich.helicon.dto.ScoreOrderDto
import org.springframework.stereotype.Controller
import java.util.*

@Controller
interface OrderController {

    fun getAll(jwt: String): List<ScoreOrderDto>
    fun getOrderById(id: UUID, jwt: String): ScoreOrderDto
    fun order(order: ScoreOrderDto): ScoreOrderDto
    fun confirm(id: UUID): ScoreOrderDto
}
