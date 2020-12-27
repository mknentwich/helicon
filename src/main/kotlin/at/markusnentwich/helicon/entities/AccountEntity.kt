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
    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], targetEntity = RoleEntity::class)
    var roles: MutableSet<RoleEntity> = mutableSetOf()
)

@Entity
@Table(name = "role")
class RoleEntity(
    @Id
    var name: String = "nobody",
    @ManyToMany(cascade = [CascadeType.ALL], mappedBy = "roles", targetEntity = AccountEntity::class)
    var accounts: MutableSet<AccountEntity> = mutableSetOf()
)
