package at.markusnentwich.helicon.controller

import at.markusnentwich.helicon.dto.CategoryProductDto
import at.markusnentwich.helicon.dto.ScoreProductDto
import org.springframework.stereotype.Controller

@Controller
interface CatalogueController {

    fun getCatalogue(): CategoryProductDto
    fun getCategory(id: Long, embed: Boolean): CategoryProductDto
    fun createCategory(category: CategoryProductDto): CategoryProductDto
    fun updateCategory(category: CategoryProductDto, id: Long): CategoryProductDto
    fun deleteCategory(id: Long, force: Boolean)

    fun getScores(): List<ScoreProductDto>
    fun getScore(id: Long): ScoreProductDto
    fun createScore(score: ScoreProductDto): ScoreProductDto
    fun updateScore(score: ScoreProductDto, id: Long): ScoreProductDto
    fun deleteScore(id: Long)
}
