package at.markusnentwich.helicon.services

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.io.File

@RestController(ASSET_SERVICE)
class AssetServiceImpl : AssetService {
    override fun getScoreAudio(id: Long) {
        TODO("Not yet implemented")
    }

    override fun updateScoreAudio(id: Long, audio: File): ResponseEntity<Void> {
        TODO("Not yet implemented")
    }

    override fun deleteScoreAudio(id: Long): ResponseEntity<Void> {
        TODO("Not yet implemented")
    }

    override fun getScorePdf(id: Long) {
        TODO("Not yet implemented")
    }

    override fun updateScorePdf(id: Long, pdf: File): ResponseEntity<Void> {
        TODO("Not yet implemented")
    }

    override fun deleteScorePdf(id: Long): ResponseEntity<Void> {
        TODO("Not yet implemented")
    }
}
