package at.markusnentwich.helicon.entities

import javax.persistence.*

@Entity
@Table(name = "address")
data class AddressEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long,
        val city: String,
        val postCode: String,
        val street: String,
        val streetNumber: String,
        @ManyToOne
        val state: StateEntity
)