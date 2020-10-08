package at.markusnentwich.helicon.entities

import java.io.Serializable
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "product_order")
class OrderEntity(
        @Id
        @GeneratedValue()
        var id: UUID? = null,
        var confirmed: LocalDateTime? = null,
        @ManyToOne
        var customer: AccountEntity? = null,
        @ManyToOne
        var deliveryAddress: AddressEntity? = null,
        @ManyToOne
        var identity: IdentityEntity = IdentityEntity(),
        @OneToMany
        var items: List<OrderScoreEntity> = listOf(),
        var inProgress: LocalDateTime? = null,
        var receivedOn: LocalDateTime = LocalDateTime.now(),
        var sent: LocalDateTime? = null
)

class OrderScorePK(
        var order: UUID? = null,
        var score: Long? = null
) : Serializable

@Entity
@Table(name = "order_score")
@IdClass(OrderScorePK::class)
class OrderScoreEntity(
        @Id
        @ManyToOne
        var score: ScoreEntity,
        @Id
        @ManyToOne
        var order: OrderEntity,
        var amount: Int = 1
)