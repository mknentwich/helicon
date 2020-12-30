package at.markusnentwich.helicon.controller

import at.markusnentwich.helicon.dto.CategoryProductDto
import at.markusnentwich.helicon.dto.ScoreProductDto
import at.markusnentwich.helicon.entities.CategoryEntity
import at.markusnentwich.helicon.entities.ScoreEntity
import at.markusnentwich.helicon.repositories.CategoryRepository
import at.markusnentwich.helicon.repositories.ScoreRepository
import org.modelmapper.ModelMapper
import org.modelmapper.TypeToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import java.lang.reflect.Type

@Controller
class CatalogueControllerImpl(
    @Autowired val scoreRepository: ScoreRepository,
    @Autowired val categoryRepository: CategoryRepository,
    @Autowired val mapper: ModelMapper
) : CatalogueController {
    override fun getCatalogue(): CategoryProductDto {
        return mapper.map(categoryRepository.getRoot(), CategoryProductDto::class.java)
    }

    override fun getCategory(id: Long, embed: Boolean): CategoryProductDto {
        val categoryEntity = categoryRepository.findByIdOrNull(id) ?: throw NotFoundException()
        return mapper.map(categoryEntity, CategoryProductDto::class.java)
    }

    override fun createCategory(category: CategoryProductDto): CategoryProductDto {
        val categoryEntity = mapper.map(category, CategoryEntity::class.java)
        if (category.parent?.id != null) {
            categoryEntity.parent = categoryRepository.findByIdOrNull(category.parent!!.id!!) ?: throw BadPayloadException()
        }
        return mapper.map(categoryRepository.save(categoryEntity), CategoryProductDto::class.java)
    }

    override fun updateCategory(category: CategoryProductDto, id: Long): CategoryProductDto {
        val categoryEntity = mapper.map(category, CategoryEntity::class.java)
        TODO("Not yes implemented")
    }

    override fun deleteCategory(id: Long, force: Boolean) {
        val categoryEntity = categoryRepository.findByIdOrNull(id) ?: throw NotFoundException()
        if ((categoryEntity.scores?.size != 0 || categoryEntity.children?.size != 0) && !force) {
            throw DependentEntriesException()
        }
        categoryRepository.deleteById(id)
    }

    override fun getScores(): List<ScoreProductDto> {
        val listType: Type = object : TypeToken<List<ScoreProductDto>>() {}.type
        return mapper.map(scoreRepository.findAll(), listType)
    }

    override fun getScore(id: Long): ScoreProductDto {
        val scoreEntity = scoreRepository.findByIdOrNull(id) ?: throw NotFoundException()
        scoreEntity.category.scores = null
        return mapper.map(scoreEntity, ScoreProductDto::class.java)
    }

    override fun createScore(score: ScoreProductDto): ScoreProductDto {
        val scoreEntity = mapper.map(score, ScoreEntity::class.java)
        if (scoreEntity.category.id == null) {
            throw BadPayloadException()
        }
        scoreEntity.category = categoryRepository.findByIdOrNull(scoreEntity.category.id!!) ?: throw BadPayloadException()
        scoreEntity.category.scores = null
        return mapper.map(scoreRepository.save(scoreEntity), ScoreProductDto::class.java)
    }

    override fun updateScore(score: ScoreProductDto, id: Long): ScoreProductDto {
        TODO("Not yet implemented")
    }

    override fun deleteScore(id: Long) {
        TODO("Not yet implemented")
    }
}
