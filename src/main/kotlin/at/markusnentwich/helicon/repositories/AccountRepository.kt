package at.markusnentwich.helicon.repositories

import at.markusnentwich.helicon.entities.AccountEntity
import at.markusnentwich.helicon.entities.IdentityEntity
import at.markusnentwich.helicon.entities.RoleEntity
import org.springframework.data.repository.CrudRepository

interface AccountRepository : CrudRepository<AccountEntity, String>

interface RoleRepository : CrudRepository<RoleEntity, Long>

interface IdentityRepository : CrudRepository<IdentityEntity, Long>
