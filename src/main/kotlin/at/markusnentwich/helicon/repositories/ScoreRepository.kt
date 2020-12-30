package at.markusnentwich.helicon.repositories

import at.markusnentwich.helicon.entities.ScoreEntity
import org.springframework.data.repository.CrudRepository

interface ScoreRepository : CrudRepository<ScoreEntity, Long>
