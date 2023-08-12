package at.markusnentwich.helicon.entities

import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name = "state")
class StateEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    var name: String = "Österreich",
    @Column(precision = 6, scale = 3) // allows values up to 999.999 in 0.001 steps
    var bookTaxes: BigDecimal? = BigDecimal.valueOf(10),
    @ManyToOne var zone: ZoneEntity = ZoneEntity()
)
