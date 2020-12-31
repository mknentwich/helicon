package at.markusnentwich.helicon.repositories

import at.markusnentwich.helicon.entities.AccountEntity
import at.markusnentwich.helicon.entities.IdentityEntity
import at.markusnentwich.helicon.entities.RoleEntity
import at.markusnentwich.helicon.security.MONITOR_ROLE
import at.markusnentwich.helicon.security.OWNER_ROLE
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface AccountRepository : CrudRepository<AccountEntity, String> {

    @Query("select ae from AccountEntity ae inner join ae.roles r where r.name = '$OWNER_ROLE'")
    fun getOwner(): AccountEntity?

    @Query("select ae from AccountEntity ae inner join ae.roles r where r.name = '$MONITOR_ROLE'")
    fun getMonitors(): MutableIterable<AccountEntity>
}

interface RoleRepository : CrudRepository<RoleEntity, String>

interface IdentityRepository : CrudRepository<IdentityEntity, Long>
