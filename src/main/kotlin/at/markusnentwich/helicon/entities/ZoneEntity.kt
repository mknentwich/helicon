package at.markusnentwich.helicon.entities

import javax.persistence.*

@Entity
@Table(name = "zone")
data class ZoneEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Int,
        val name: String,
        val shipping: Int,
)