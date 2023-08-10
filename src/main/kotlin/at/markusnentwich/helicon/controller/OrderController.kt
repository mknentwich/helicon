package at.markusnentwich.helicon.controller

import at.markusnentwich.helicon.dto.ScoreOrderDto
import org.springframework.stereotype.Controller
import java.time.LocalDate
import java.util.*

@Controller
interface OrderController {

    fun getAll(jwt: String): List<ScoreOrderDto>
    fun getOrderById(id: UUID, jwt: String): ScoreOrderDto
    fun order(order: ScoreOrderDto): ScoreOrderDto
    fun confirm(id: UUID): ScoreOrderDto
    fun billCollection(
        from: LocalDate,
        to: LocalDate,
        confirmed: Boolean,
        jwt: String
    ): ByteArray
}
