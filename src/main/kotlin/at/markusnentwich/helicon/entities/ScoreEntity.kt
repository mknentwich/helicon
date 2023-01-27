package at.markusnentwich.helicon.entities

import jakarta.persistence.*

@Entity
@Table(name = "score")
class ScoreEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    var author: String = "Markus Nentwich",
    @ManyToOne
    var category: CategoryEntity = CategoryEntity(),
    var difficulty: Int = 1,
    var groupType: String = "Blasorchester",
    @Column(length = 10000)
    var instrumentation: String = "",
    var price: Int = 0,
    var title: String = "default title",
    @OneToMany(mappedBy = "score")
    var orders: MutableSet<OrderScoreEntity> = mutableSetOf(),
    var summary: String? = null,
    @Column(length = 10000)
    var description: String? = null
)
