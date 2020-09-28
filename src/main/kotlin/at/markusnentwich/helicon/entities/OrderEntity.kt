package at.markusnentwich.helicon.entities

import java.io.Serializable
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "product_order")
data class OrderEntity(
        @Id
        @GeneratedValue()
        val id: UUID,
        @ManyToOne
        val billingAddress: AddressEntity,
        val confirmed: LocalDateTime?,
        @ManyToOne
        val deliveryAddress: AddressEntity,
        @ManyToOne
        val identity: IdentityEntity,
        @OneToMany
        val items: List<OrderScoreEntity>,
        val inProgress: LocalDateTime?,
        val receivedOn: LocalDateTime,
        val sent: LocalDateTime?
)

data class OrderScorePK(val order: UUID, val score: Int) : Serializable

@Entity
@Table(name = "order_score")
@IdClass(OrderScorePK::class)
data class OrderScoreEntity(

        @Id
        @ManyToOne
        val score: ScoreEntity,
        @Id
        @ManyToOne
        val order: OrderEntity,
        val amount: Int,
)