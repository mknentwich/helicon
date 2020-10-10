package at.markusnentwich.helicon.entities

import javax.persistence.*

@Entity
@Table(name = "state")
class StateEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    var name: String = "Österreich",
    @ManyToOne var zone: ZoneEntity = ZoneEntity()
)
