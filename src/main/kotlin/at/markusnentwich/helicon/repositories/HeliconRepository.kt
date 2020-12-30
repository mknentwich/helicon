package at.markusnentwich.helicon.repositories

import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.NoRepositoryBean
import java.io.Serializable
import javax.persistence.EntityManager
import javax.transaction.Transactional

@NoRepositoryBean
interface HeliconRepository<T, ID : Serializable> : CrudRepository<T, ID> {
    fun refresh(t: T)
}

class HeliconRepositoryImpl<T, ID : Serializable>(
    entityInformation: JpaEntityInformation<T, ID>,
    private val entityManager: EntityManager
) : HeliconRepository<T, ID>, SimpleJpaRepository<T, ID>(entityInformation, entityManager) {

    @Transactional
    override fun refresh(t: T) {
        entityManager.refresh(t)
    }
}
