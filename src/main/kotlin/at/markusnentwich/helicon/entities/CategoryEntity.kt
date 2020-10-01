package at.markusnentwich.helicon.entities

import javax.persistence.*

@Entity
@Table(name = "category")
class CategoryEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,
        var name: String = "Polka",
        var namePlural: String = "Polka",
        @ManyToOne
        var parent: CategoryEntity?
)