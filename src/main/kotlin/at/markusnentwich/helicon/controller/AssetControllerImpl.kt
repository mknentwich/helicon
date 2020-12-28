package at.markusnentwich.helicon.controller

import at.markusnentwich.helicon.configuration.HeliconConfigurationProperties
import at.markusnentwich.helicon.repositories.ScoreRepository
import org.apache.tomcat.util.http.fileupload.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import java.io.File
import java.io.InputStream
import java.nio.file.Path

const val AUDIO_NAME = "audio"
const val PDF_NAME = "pdf"

@Controller
class AssetControllerImpl(
    @Autowired val configurationProperties: HeliconConfigurationProperties,
    @Autowired val scoreRepository: ScoreRepository
) : AssetController {
    override fun getScoreAudio(id: Long): File {
        return getScoreAudioPath().resolve(id.toString()).toFile()
    }

    override fun updateScoreAudio(id: Long, stream: InputStream) {
        if (!scoreRepository.existsById(id)) {
            throw NotFoundException()
        }
        val audioFile = getScoreAudio(id).outputStream()
        IOUtils.copy(stream, audioFile)
    }

    override fun deleteScoreAudio(id: Long) {
        TODO("Not yet implemented")
    }

    override fun getScorePdf(id: Long): File {
        return getScorePdfPath().resolve(id.toString()).toFile()
    }

    override fun updateScorePdf(id: Long, stream: InputStream) {
        if (!scoreRepository.existsById(id)) {
            throw NotFoundException()
        }
        val pdfFile = getScorePdf(id).outputStream()
        IOUtils.copy(stream, pdfFile)
    }

    override fun deleteScorePdf(id: Long) {
        TODO("Not yet implemented")
    }

    fun getScoreAudioPath(): Path {
        return Path.of(configurationProperties.assets, AUDIO_NAME)
    }

    fun getScorePdfPath(): Path {
        return Path.of(configurationProperties.assets, PDF_NAME)
    }
}
