package at.markusnentwich.helicon.repositories

import at.markusnentwich.helicon.entities.AddressEntity
import org.springframework.data.repository.CrudRepository

interface AddressRepository : CrudRepository<AddressEntity, Long>
