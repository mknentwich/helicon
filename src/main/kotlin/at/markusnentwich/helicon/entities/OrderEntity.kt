package at.markusnentwich.helicon.entities

import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.Formula
import java.io.Serializable
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Column
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
    @Column(precision = 6, scale = 3)
    var taxRate: BigDecimal? = null,
    @ColumnDefault("0")
    var shipping: Int = 0,
    var inProgress: LocalDateTime? = null,
    var receivedOn: LocalDateTime? = null,
    var sent: LocalDateTime? = null,
    @Formula("to_char(confirmed, 'yyyyMMdd') || to_char((select count(*) from product_order po where not (id = po.id) and po.confirmed is not null and po.confirmed < confirmed and date_trunc('day', po.confirmed) = date_trunc('day', confirmed)) + 1, 'FM00')")
    var billingNumber: String? = null
) {
    fun deliveryAddress(): AddressEntity {
        return deliveryAddress ?: identity.address
    }

    fun total(): Int {
        return productCosts() + shipping
    }

    fun productCosts(): Int {
        return items.stream().mapToInt { it.amount * it.score.price }.sum()
    }

    fun beforeTaxes(): Int {
        val mc = MathContext(30, RoundingMode.HALF_UP)
        val taxRate = 100.toBigDecimal(mc) + (taxRate ?: BigDecimal.ZERO)
        if (taxRate == 100.toBigDecimal(mc)) {
            return total()
        }
        val total = total().toBigDecimal(mc)
        return total.divide(taxRate, mc).multiply(100.toBigDecimal(mc), mc).setScale(0, RoundingMode.HALF_UP)
            .intValueExact()
    }

    fun taxes(): Int {
        return total() - beforeTaxes()
    }
}

class OrderScorePK(
    var order: UUID? = null,
    var score: Long? = null
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderScorePK

        if (order != other.order) return false
        if (score != other.score) return false

        return true
    }

    override fun hashCode(): Int {
        var result = order?.hashCode() ?: 0
        result = 31 * result + (score?.hashCode() ?: 0)
        return result
    }
}

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
