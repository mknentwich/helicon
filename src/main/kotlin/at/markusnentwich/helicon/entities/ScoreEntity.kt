package at.markusnentwich.helicon.entities

import javax.persistence.*

@Entity
@Table(name = "score")
data class ScoreEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Int,
        @ManyToOne
        val category: CategoryEntity,
        val difficulty: Int,
        val instrumentation: String,
        val price: Int,
        val title: String
)