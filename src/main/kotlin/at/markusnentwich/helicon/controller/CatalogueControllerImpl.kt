package at.markusnentwich.helicon.controller

import at.markusnentwich.helicon.dto.CategoryProductDto
import at.markusnentwich.helicon.dto.ScoreProductDto
import at.markusnentwich.helicon.entities.CategoryEntity
import at.markusnentwich.helicon.entities.ROOT_CATEGORY_NAME
import at.markusnentwich.helicon.entities.ScoreEntity
import at.markusnentwich.helicon.repositories.CategoryRepository
import at.markusnentwich.helicon.repositories.ScoreRepository
import org.modelmapper.ModelMapper
import org.modelmapper.TypeToken
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import java.lang.reflect.Type

@Controller
class CatalogueControllerImpl(
    @Autowired val scoreRepository: ScoreRepository,
    @Autowired val categoryRepository: CategoryRepository,
    @Autowired val orderScoreRepository: ScoreRepository,
    @Autowired val mapper: ModelMapper
) : CatalogueController {
    private val logger = LoggerFactory.getLogger(CatalogueControllerImpl::class.java)

    override fun getCatalogue(): CategoryProductDto {
        val root = categoryRepository.getRoot() ?: throw NotFoundException()
        return mapper.map(root, CategoryProductDto::class.java)
    }

    override fun getCategory(id: Long, embed: Boolean): CategoryProductDto {
        val categoryEntity = categoryRepository.findByIdOrNull(id) ?: throw NotFoundException()
        return mapper.map(categoryEntity, CategoryProductDto::class.java)
    }

    override fun createCategory(category: CategoryProductDto): CategoryProductDto {
        val categoryEntity = mapper.map(category, CategoryEntity::class.java)
        if (categoryRepository.getRoot() != null && categoryEntity.name == ROOT_CATEGORY_NAME) {
            throw BadPayloadException()
        }
        categoryEntity.parent = categoryRepository.findByIdOrNull(category.parentId!!)
        val saved = categoryRepository.save(categoryEntity)
        return mapper.map(saved, CategoryProductDto::class.java)
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
        scoreEntity.category =
            categoryRepository.findByIdOrNull(score.categoryId!!) ?: throw BadPayloadException()
        scoreEntity.category.scores = null
        return mapper.map(scoreRepository.save(scoreEntity), ScoreProductDto::class.java)
    }

    override fun updateScore(score: ScoreProductDto, id: Long): ScoreProductDto {
        val scoreEntity = mapper.map(score, ScoreEntity::class.java)
        if (!scoreRepository.existsById(id)) {
            logger.error("Cannot update score with id [{}], it does not exist", id)
            throw NotFoundException()
        }
        val category = categoryRepository.findByIdOrNull(score.categoryId ?: scoreEntity.category.id!!)
            ?: throw BadPayloadException()
        scoreEntity.id = id
        scoreEntity.category = category
        return mapper.map(scoreRepository.save(scoreEntity), ScoreProductDto::class.java)
    }

    override fun deleteScore(id: Long) {
        val scoreEntity = scoreRepository.findByIdOrNull(id)
        if (scoreEntity == null) {
            logger.error("Cannot delete score with id [{}], it does not exist", id)
            throw NotFoundException()
        }
        if (scoreEntity.orders.isNotEmpty()) {
            logger.error(
                "Cannot delete score [{}] - [{}] as it is used in [{}] orders",
                scoreEntity.id,
                scoreEntity.title,
                scoreEntity.orders.size
            )
            throw BadPayloadException()
        }
        scoreRepository.deleteById(id)
        logger.debug("Deleted score [{}] - [{}]", scoreEntity.id, scoreEntity.title)
    }
}
