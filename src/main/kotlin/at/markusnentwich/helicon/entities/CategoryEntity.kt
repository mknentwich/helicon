package at.markusnentwich.helicon.entities

import javax.persistence.*

@Entity
@Table(name = "category")
data class CategoryEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Int,
        val name: String,
        val namePlural: String,
        @ManyToOne
        val parent: CategoryEntity?,
)