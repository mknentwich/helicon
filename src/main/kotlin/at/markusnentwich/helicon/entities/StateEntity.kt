package at.markusnentwich.helicon.entities

import javax.persistence.*

@Entity
@Table(name = "state")
data class StateEntity(

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Int,
        val name: String,
        @ManyToOne
        val zone: ZoneEntity
)