package at.markusnentwich.helicon.controller

import org.springframework.stereotype.Controller
import java.io.File
import java.io.InputStream

@Controller
interface AssetController {
    fun getScoreAudio(id: Long): File
    fun updateScoreAudio(id: Long, stream: InputStream)
    fun deleteScoreAudio(id: Long)
    fun getScorePdf(id: Long): File
    fun updateScorePdf(id: Long, stream: InputStream)
    fun deleteScorePdf(id: Long)
}
