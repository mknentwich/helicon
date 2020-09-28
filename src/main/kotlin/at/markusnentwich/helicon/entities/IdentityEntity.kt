package at.markusnentwich.helicon.entities

import javax.persistence.*

@Entity
@Table(name = "identity")
data class IdentityEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Int,

        val firstName: String,
        val lastName: String,
        val email: String,
        val telephone: String,
        @ManyToOne
        val address: AddressEntity
)