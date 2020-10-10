package at.markusnentwich.helicon.controller

import at.markusnentwich.helicon.dto.CategoryProductDto
import at.markusnentwich.helicon.dto.ScoreProductDto
import org.springframework.stereotype.Controller

@Controller
interface CatalogueController {

    fun getCatalogue(): CategoryProductDto
    fun getCategory(id: Long, embed: Boolean): CategoryProductDto
    fun createCategory(category: CategoryProductDto, jwt: String): CategoryProductDto
    fun updateCategory(category: CategoryProductDto, id: Long, jwt: String): CategoryProductDto
    fun deleteCategory(id: Long, jwt: String)

    fun getScores(): List<ScoreProductDto>
    fun getScore(id: Long): ScoreProductDto
    fun createScore(score: ScoreProductDto, jwt: String): ScoreProductDto
    fun updateScore(score: ScoreProductDto, id: Long, jwt: String): ScoreProductDto
    fun deleteScore(id: Long, jwt: String)
}
