package at.markusnentwich.helicon.services

import at.markusnentwich.helicon.controller.AssetController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

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

    override fun updateScoreAudio(id: Long, audio: File): ResponseEntity<Void> {
        TODO("Not yet implemented")
    }

    override fun deleteScoreAudio(id: Long): ResponseEntity<Void> {
        TODO("Not yet implemented")
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

    override fun updateScorePdf(id: Long, pdf: File): ResponseEntity<Void> {
        TODO("Not yet implemented")
    }

    override fun deleteScorePdf(id: Long): ResponseEntity<Void> {
        TODO("Not yet implemented")
    }
}
