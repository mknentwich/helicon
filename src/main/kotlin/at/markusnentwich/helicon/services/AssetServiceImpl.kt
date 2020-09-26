package at.markusnentwich.helicon.services

import org.springframework.web.bind.annotation.RestController
import java.io.File

@RestController
class AssetServiceImpl: AssetService {
    override fun getScoreAudio(id: Int) {
        TODO("Not yet implemented")
    }

    override fun setScoreAudio(id: Int, audio: File) {
        TODO("Not yet implemented")
    }

    override fun deleteScoreAudio(id: Int) {
        TODO("Not yet implemented")
    }

    override fun getScorePdf(id: Int) {
        TODO("Not yet implemented")
    }

    override fun setScorePdf(id: Int, pdf: File) {
        TODO("Not yet implemented")
    }

    override fun deleteScorePdf(id: Int) {
        TODO("Not yet implemented")
    }
}