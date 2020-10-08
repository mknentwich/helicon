package at.markusnentwich.helicon.entities

import javax.persistence.*

@Entity
@Table(name = "score")
class ScoreEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,
        @ManyToOne
        var category: CategoryEntity = CategoryEntity(),
        var difficulty: Int = 1,
        var instrumentation: String = "",
        var price: Int = 0,
        var title: String = "default title"
)