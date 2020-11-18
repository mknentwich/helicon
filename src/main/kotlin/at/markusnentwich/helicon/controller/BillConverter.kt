package at.markusnentwich.helicon.controller

import at.markusnentwich.helicon.entities.IdentityEntity
import at.markusnentwich.helicon.entities.OrderEntity
import org.springframework.stereotype.Controller

@Controller
interface BillConverter {
    fun createBill(order: OrderEntity, owner: IdentityEntity): Array<Byte>
}
