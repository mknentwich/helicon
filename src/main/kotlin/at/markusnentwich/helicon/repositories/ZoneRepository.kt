package at.markusnentwich.helicon.repositories

import at.markusnentwich.helicon.entities.ZoneEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ZoneRepository : CrudRepository<ZoneEntity, Long> {
}