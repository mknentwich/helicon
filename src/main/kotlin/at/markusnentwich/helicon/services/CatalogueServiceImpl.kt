package at.markusnentwich.helicon.services

import at.markusnentwich.helicon.dto.CategoryProductDto
import at.markusnentwich.helicon.dto.ScoreProductDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class CatalogueServiceImpl() : CatalogueService {

    override fun getCatalogue(): ResponseEntity<CategoryProductDto> {
        TODO("Not yet implemented")
    }

    override fun getCategory(id: Long, embed: Boolean): ResponseEntity<CategoryProductDto> {
        TODO("Not yet implemented")
    }

    override fun createCategory(category: CategoryProductDto): ResponseEntity<CategoryProductDto> {
        TODO("Not yet implemented")
    }

    override fun updateCategory(category: CategoryProductDto, id: Long): ResponseEntity<CategoryProductDto> {
        TODO("Not yet implemented")
    }

    override fun deleteCategory(id: Long) {
        TODO("Not yet implemented")
    }

    override fun getScores(): ResponseEntity<Iterable<ScoreProductDto>> {
        TODO("Not yet implemented")
    }

    override fun getScoreById(id: Long): ResponseEntity<ScoreProductDto> {
        TODO("Not yet implemented")
    }

    override fun createScore(score: ScoreProductDto): ResponseEntity<ScoreProductDto> {
        TODO("Not yet implemented")
    }

    override fun updateScore(id: Long): ResponseEntity<ScoreProductDto> {
        TODO("Not yet implemented")
    }

    override fun deleteScore(id: Long) {
        TODO("Not yet implemented")
    }
}