package at.markusnentwich.helicon.repositories

import at.markusnentwich.helicon.entities.CategoryEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface CategoryRepository : CrudRepository<CategoryEntity, Long> {

    @Query("from CategoryEntity p inner join CategoryEntity c on c.parent = p where p.parent is null")
    fun getRoot(): CategoryEntity
}
