package at.markusnentwich.helicon.repositories

import at.markusnentwich.helicon.entities.CategoryEntity
import at.markusnentwich.helicon.entities.ROOT_CATEGORY_NAME
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface CategoryRepository : CrudRepository<CategoryEntity, Long> {

    @Query("from CategoryEntity p where p.name = '$ROOT_CATEGORY_NAME'")
    fun getRoot(): CategoryEntity?
}
