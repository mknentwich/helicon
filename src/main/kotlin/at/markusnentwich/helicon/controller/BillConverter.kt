package at.markusnentwich.helicon.controller

import at.markusnentwich.helicon.entities.OrderEntity
import org.springframework.stereotype.Controller
import java.io.InputStream

@Controller
interface BillConverter {
    fun createBill(order: OrderEntity): InputStream
}
