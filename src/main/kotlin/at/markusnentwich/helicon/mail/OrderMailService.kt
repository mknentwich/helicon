package at.markusnentwich.helicon.mail

import at.markusnentwich.helicon.entities.OrderEntity
import org.springframework.stereotype.Service

@Service
interface OrderMailService {
    fun notifyCustomer(order: OrderEntity)
    fun notifyOwner(order: OrderEntity)
}
