package at.markusnentwich.helicon.entities

import javax.persistence.*

@Entity
@Table(name = "account")
class AccountEntity(
    @Id
    var username: String = "karli",
    var password: String = "invalid",
    @ManyToOne
    var identity: IdentityEntity = IdentityEntity(),
    @ManyToMany
    var roles: List<RoleEntity> = listOf()
)

@Entity
@Table(name = "role")
class RoleEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    var name: String = "nobody"
)
