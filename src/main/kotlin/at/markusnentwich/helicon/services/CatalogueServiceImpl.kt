package at.markusnentwich.helicon.services

import at.markusnentwich.helicon.controller.AlreadyExistsException
import at.markusnentwich.helicon.controller.BadPayloadException
import at.markusnentwich.helicon.controller.CatalogueController
import at.markusnentwich.helicon.controller.NotFoundException
import at.markusnentwich.helicon.dto.CategoryProductDto
import at.markusnentwich.helicon.dto.ScoreProductDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController(CATALOGUE_SERVICE)
class CatalogueServiceImpl(
    @Autowired val catalogueController: CatalogueController
) : CatalogueService {
    val logger = LoggerFactory.getLogger(CatalogueServiceImpl::class.java)

    override fun getCatalogue(): ResponseEntity<CategoryProductDto> {
        callLog("getCatalogue")
        return ResponseEntity.ok(catalogueController.getCatalogue())
    }

    override fun getCategory(id: Long, embed: Boolean): ResponseEntity<CategoryProductDto> {
        callLog("getCategory")
        val status = try {
            return ResponseEntity.ok(catalogueController.getCategory(id, embed))
        } catch (e: NotFoundException) {
            HttpStatus.NOT_FOUND
        }
        return ResponseEntity.status(status).build()
    }

    override fun createCategory(category: CategoryProductDto, jwt: String): ResponseEntity<CategoryProductDto> {
        callLog("createCategory")
        val status = try {
            return ResponseEntity.ok(catalogueController.createCategory(category, jwt))
        } catch (e: BadPayloadException) {
            HttpStatus.BAD_REQUEST
        }
        return ResponseEntity.status(status).build()
    }

    override fun updateCategory(
        category: CategoryProductDto,
        id: Long,
        jwt: String
    ): ResponseEntity<CategoryProductDto> {
        callLog("updateCategory")
        val status = try {
            return ResponseEntity.ok(catalogueController.updateCategory(category, id, jwt))
        } catch (e: NotFoundException) {
            HttpStatus.NOT_FOUND
        } catch (e: BadPayloadException) {
            HttpStatus.BAD_REQUEST
        } catch (e: AlreadyExistsException) {
            HttpStatus.CONFLICT
        }
        return ResponseEntity.status(status).build()
    }

    override fun deleteCategory(id: Long, jwt: String): ResponseEntity<Void> {
        callLog("deleteCategory")
        val status = try {
            catalogueController.deleteCategory(id, jwt)
            HttpStatus.OK
        } catch (e: NotFoundException) {
            HttpStatus.NOT_FOUND
        }
        return ResponseEntity.status(status).build()
    }

    override fun getScores(): ResponseEntity<Iterable<ScoreProductDto>> {
        callLog("getScores")
        return ResponseEntity.ok(catalogueController.getScores())
    }

    override fun getScoreById(id: Long): ResponseEntity<ScoreProductDto> {
        callLog("getScoreById")
        val status = try {
            return ResponseEntity.ok(catalogueController.getScore(id))
        } catch (e: NotFoundException) {
            HttpStatus.NOT_FOUND
        }
        return ResponseEntity.status(status).build()
    }

    override fun createScore(score: ScoreProductDto, jwt: String): ResponseEntity<ScoreProductDto> {
        callLog("createScore")
        val status = try {
            return ResponseEntity.ok(catalogueController.createScore(score, jwt))
        } catch (e: BadPayloadException) {
            HttpStatus.BAD_REQUEST
        }
        return ResponseEntity.status(status).build()
    }

    override fun updateScore(score: ScoreProductDto, id: Long, jwt: String): ResponseEntity<ScoreProductDto> {
        callLog("updateScore")
        val status = try {
            return ResponseEntity.ok(catalogueController.updateScore(score, id, jwt))
        } catch (e: NotFoundException) {
            HttpStatus.NOT_FOUND
        } catch (e: BadPayloadException) {
            HttpStatus.BAD_REQUEST
        } catch (e: AlreadyExistsException) {
            HttpStatus.CONFLICT
        }
        return ResponseEntity.status(status).build()
    }

    override fun deleteScore(id: Long, jwt: String): ResponseEntity<Void> {
        callLog("deleteScore")
        val status = try {
            catalogueController.deleteScore(id, jwt)
            HttpStatus.OK
        } catch (e: NotFoundException) {
            HttpStatus.NOT_FOUND
        }
        return ResponseEntity.status(status).build()
    }

    fun callLog(method: String) {
        logger.trace("called {}", method)
    }
}
