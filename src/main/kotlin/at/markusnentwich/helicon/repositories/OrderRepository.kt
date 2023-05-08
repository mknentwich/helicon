package at.markusnentwich.helicon.repositories

import at.markusnentwich.helicon.entities.OrderEntity
import at.markusnentwich.helicon.entities.OrderScoreEntity
import at.markusnentwich.helicon.entities.OrderScorePK
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Stream

@Repository
interface OrderRepository : HeliconRepository<OrderEntity, UUID> {

    @Query("select o from OrderEntity o where o.receivedOn >= :from and o.receivedOn < :to and (o.confirmed is not null or :onlyConfirmed = false)")
    fun findOrdersInRange(from: LocalDateTime, to: LocalDateTime, onlyConfirmed: Boolean): Stream<OrderEntity>
}

@Repository
interface OrderScoreRepository : CrudRepository<OrderScoreEntity, OrderScorePK>
