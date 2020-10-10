package at.markusnentwich.helicon.repositories

import at.markusnentwich.helicon.entities.StateEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface StateRepository : CrudRepository<StateEntity, Long>
