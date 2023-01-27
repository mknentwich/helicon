package at.markusnentwich.helicon.entities

import jakarta.persistence.*

@Entity
@Table(name = "zone")
class ZoneEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    var name: String = "EU",
    var shipping: Int = 700
)
