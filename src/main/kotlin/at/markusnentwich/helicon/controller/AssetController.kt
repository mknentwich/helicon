package at.markusnentwich.helicon.controller

import org.springframework.stereotype.Controller
import java.io.File

@Controller
interface AssetController {
    fun getScoreAudio(id: Long): File
    fun updateScoreAudio(id: Long, audio: File)
    fun deleteScoreAudio(id: Long)
    fun getScorePdf(id: Long): File
    fun updateScorePdf(id: Long, pdf: File)
    fun deleteScorePdf(id: Long)
}
