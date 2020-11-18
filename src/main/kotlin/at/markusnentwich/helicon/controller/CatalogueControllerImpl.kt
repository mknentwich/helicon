package at.markusnentwich.helicon.controller

import at.markusnentwich.helicon.dto.CategoryProductDto
import at.markusnentwich.helicon.dto.ScoreProductDto
import org.springframework.stereotype.Controller

@Controller
class CatalogueControllerImpl : CatalogueController {
    override fun getCatalogue(): CategoryProductDto {
        TODO("Not yet implemented")
    }

    override fun getCategory(id: Long, embed: Boolean): CategoryProductDto {
        TODO("Not yet implemented")
    }

    override fun createCategory(category: CategoryProductDto, jwt: String): CategoryProductDto {
        TODO("Not yet implemented")
    }

    override fun updateCategory(category: CategoryProductDto, id: Long, jwt: String): CategoryProductDto {
        TODO("Not yet implemented")
    }

    override fun deleteCategory(id: Long, jwt: String) {
        TODO("Not yet implemented")
    }

    override fun getScores(): List<ScoreProductDto> {
        TODO("Not yet implemented")
    }

    override fun getScore(id: Long): ScoreProductDto {
        TODO("Not yet implemented")
    }

    override fun createScore(score: ScoreProductDto, jwt: String): ScoreProductDto {
        TODO("Not yet implemented")
    }

    override fun updateScore(score: ScoreProductDto, id: Long, jwt: String): ScoreProductDto {
        TODO("Not yet implemented")
    }

    override fun deleteScore(id: Long, jwt: String) {
        TODO("Not yet implemented")
    }
}
