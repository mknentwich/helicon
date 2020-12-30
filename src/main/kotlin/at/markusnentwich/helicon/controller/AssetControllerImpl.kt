package at.markusnentwich.helicon.controller

import at.markusnentwich.helicon.configuration.HeliconConfigurationProperties
import at.markusnentwich.helicon.repositories.ScoreRepository
import org.apache.tomcat.util.http.fileupload.IOUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.file.Path

const val AUDIO_NAME = "audio"
const val PDF_NAME = "pdf"

@Controller
class AssetControllerImpl(
    @Autowired val configurationProperties: HeliconConfigurationProperties,
    @Autowired val scoreRepository: ScoreRepository
) : AssetController {

    private val logger = LoggerFactory.getLogger(AssetControllerImpl::class.java)

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
        if (!scoreRepository.existsById(id)) {
            throw NotFoundException()
        }
        val audioFile = getScoreAudio(id)
        if (!audioFile.exists()) {
            throw NotFoundException()
        }
        if (!audioFile.delete()) {
            logger.error("cannot delete {}", audioFile.absolutePath)
            throw IOException("got 'false' from file delete")
        }
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
        if (!scoreRepository.existsById(id)) {
            throw NotFoundException()
        }
        val pdfFile = getScorePdf(id)
        if (!pdfFile.exists()) {
            throw NotFoundException()
        }
        if (!pdfFile.delete()) {
            logger.error("cannot delete {}", pdfFile.absolutePath)
            throw IOException("got 'false' from file delete")
        }
    }

    fun getScoreAudioPath(): Path {
        return Path.of(configurationProperties.assets, AUDIO_NAME)
    }

    fun getScorePdfPath(): Path {
        return Path.of(configurationProperties.assets, PDF_NAME)
    }
}
