package at.markusnentwich.helicon.entities

import org.hibernate.annotations.Formula
import java.io.Serializable
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

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
    @OneToMany(mappedBy = "order")
    var items: MutableSet<OrderScoreEntity> = mutableSetOf(),
    var inProgress: LocalDateTime? = null,
    var receivedOn: LocalDateTime? = null,
    var sent: LocalDateTime? = null,
    @Formula("to_char(confirmed, 'yyyyMMdd') || to_char((select count(*) from product_order po where not (id = po.id) and po.confirmed is not null and po.confirmed < confirmed and date_trunc('day', po.confirmed) = date_trunc('day', confirmed)) + 1, 'FM00')")
    var billingNumber: String? = null,
) {
    fun deliveryAddress(): AddressEntity {
        return deliveryAddress ?: identity.address
    }

    fun total(): Int {
        return items.stream().mapToInt { it.amount * it.score.price }.sum() + deliveryAddress().state.zone.shipping
    }
}

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
