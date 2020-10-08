package at.markusnentwich.helicon.entities

import javax.persistence.*

@Entity
@Table(name = "address")
class AddressEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,
        var city: String = "Leopoldsdorf",
        var postCode: String = "2285",
        var street: String = "Kempfendorf",
        var streetNumber: String = "2",
        @ManyToOne
        var state: StateEntity = StateEntity()
)