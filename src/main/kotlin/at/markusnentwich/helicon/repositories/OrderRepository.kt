package at.markusnentwich.helicon.repositories

import at.markusnentwich.helicon.entities.OrderEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface OrderRepository : CrudRepository<OrderEntity, UUID>