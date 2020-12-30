package at.markusnentwich.helicon.repositories

import at.markusnentwich.helicon.entities.OrderEntity
import at.markusnentwich.helicon.entities.OrderScoreEntity
import at.markusnentwich.helicon.entities.OrderScorePK
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface OrderRepository : HeliconRepository<OrderEntity, UUID>

@Repository
interface OrderScoreRepository : CrudRepository<OrderScoreEntity, OrderScorePK>
