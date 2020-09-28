package at.markusnentwich.helicon.services

import org.springframework.web.bind.annotation.RestController
import java.io.File

@RestController
class AssetServiceImpl: AssetService {
    override fun getScoreAudio(id: Long) {
        TODO("Not yet implemented")
    }

    override fun updateScoreAudio(id: Long, audio: File) {
        TODO("Not yet implemented")
    }

    override fun deleteScoreAudio(id: Long) {
        TODO("Not yet implemented")
    }

    override fun getScorePdf(id: Long) {
        TODO("Not yet implemented")
    }

    override fun updateScorePdf(id: Long, pdf: File) {
        TODO("Not yet implemented")
    }

    override fun deleteScorePdf(id: Long) {
        TODO("Not yet implemented")
    }
}