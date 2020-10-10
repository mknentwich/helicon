package at.markusnentwich.helicon.controller

import at.markusnentwich.helicon.dto.StateDto
import at.markusnentwich.helicon.dto.ZoneDto
import org.springframework.stereotype.Controller

@Controller
interface MetaController {
    fun getAllStates(): List<StateDto>
    fun getStateById(id: Long): StateDto
    fun createState(state: StateDto, jwt: String): StateDto
    fun updateState(state: StateDto, id: Long, jwt: String): StateDto
    fun deleteState(id: Long, jwt: String)

    fun getAllZones(): List<ZoneDto>
    fun getZoneById(id: Long): ZoneDto
    fun createZone(zone: ZoneDto, jwt: String): ZoneDto
    fun updateZone(zone: ZoneDto, id: Long, jwt: String): ZoneDto
    fun deleteZone(id: Long, jwt: String)
}
