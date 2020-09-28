package at.markusnentwich.helicon.entities

import javax.persistence.*

@Entity
@Table(name = "account")
data class AccountEntity(
        @Id
        val username: String,
        val password: String,
        @ManyToOne
        val identity: IdentityEntity,
        val state: String,
        @ManyToMany
        val roles: List<RoleEntity>
)

@Entity
@Table(name = "role")
data class RoleEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Int,
        val name: String
)