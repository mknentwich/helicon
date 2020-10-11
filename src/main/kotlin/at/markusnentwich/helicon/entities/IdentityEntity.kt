package at.markusnentwich.helicon.entities

import javax.persistence.*

@Entity
@Table(name = "identity")
class IdentityEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    var firstName: String = "Karl",
    var lastName: String = "Steinscheisser",
    var company: String? = null,
    var email: String = "karl@steinscheisser.at",
    var telephone: String = "06641234567",
    @ManyToOne
    var address: AddressEntity = AddressEntity()
)
