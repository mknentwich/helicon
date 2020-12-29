package at.markusnentwich.helicon.services

import at.markusnentwich.helicon.controller.AssetController
import at.markusnentwich.helicon.controller.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.io.FileInputStream
import java.io.FileNotFoundException
import javax.servlet.http.HttpServletRequest

@RestController(ASSET_SERVICE)
class AssetServiceImpl(
    @Autowired val assetController: AssetController
) : AssetService {
    override fun getScoreAudio(id: Long): ResponseEntity<InputStreamResource> {
        val status = try {
            val audioFile = assetController.getScoreAudio(id)
            val resource = InputStreamResource(FileInputStream(audioFile))
            return ResponseEntity.ok()
                .contentLength(audioFile.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource)
        } catch (e: FileNotFoundException) {
            HttpStatus.NOT_FOUND
        } catch (e: Exception) {
            HttpStatus.INTERNAL_SERVER_ERROR
        }
        return ResponseEntity.status(status).build()
    }

    override fun updateScoreAudio(id: Long, request: HttpServletRequest): ResponseEntity<Void> {
        val status = try {
            assetController.updateScoreAudio(id, request.inputStream)
            HttpStatus.OK
        } catch (e: NotFoundException) {
            HttpStatus.NOT_FOUND
        } catch (e: Exception) {
            HttpStatus.BAD_REQUEST
        }
        return ResponseEntity.status(status).build()
    }

    override fun deleteScoreAudio(id: Long): ResponseEntity<Void> {
        val status = try {
            assetController.deleteScoreAudio(id)
            HttpStatus.OK
        } catch (e: NotFoundException) {
            HttpStatus.NOT_FOUND
        } catch (e: Exception) {
            HttpStatus.INTERNAL_SERVER_ERROR
        }
        return ResponseEntity.status(status).build()
    }

    override fun getScorePdf(id: Long): ResponseEntity<InputStreamResource> {
        val status = try {
            val audioFile = assetController.getScorePdf(id)
            val resource = InputStreamResource(FileInputStream(audioFile))
            return ResponseEntity.ok()
                .contentLength(audioFile.length())
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource)
        } catch (e: FileNotFoundException) {
            HttpStatus.NOT_FOUND
        } catch (e: Exception) {
            HttpStatus.INTERNAL_SERVER_ERROR
        }
        return ResponseEntity.status(status).build()
    }

    override fun updateScorePdf(id: Long, request: HttpServletRequest): ResponseEntity<Void> {
        val status = try {
            assetController.updateScorePdf(id, request.inputStream)
            HttpStatus.OK
        } catch (e: NotFoundException) {
            HttpStatus.NOT_FOUND
        } catch (e: Exception) {
            HttpStatus.BAD_REQUEST
        }
        return ResponseEntity.status(status).build()
    }

    override fun deleteScorePdf(id: Long): ResponseEntity<Void> {
        val status = try {
            assetController.deleteScorePdf(id)
            HttpStatus.OK
        } catch (e: NotFoundException) {
            HttpStatus.NOT_FOUND
        } catch (e: Exception) {
            HttpStatus.INTERNAL_SERVER_ERROR
        }
        return ResponseEntity.status(status).build()
    }
}
