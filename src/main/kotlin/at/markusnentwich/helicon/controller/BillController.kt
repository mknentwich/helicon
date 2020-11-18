package at.markusnentwich.helicon.controller

import org.springframework.stereotype.Controller
import java.util.UUID

@Controller
interface BillController {
    fun bill(id: UUID, jwt: String): Array<Byte>
}
