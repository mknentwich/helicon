package at.markusnentwich.helicon.services

import at.markusnentwich.helicon.dto.StateDto
import at.markusnentwich.helicon.dto.ZoneDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class MetaServiceImpl: MetaService {
    override fun getAllStates(): ResponseEntity<Iterable<StateDto>> {
        TODO("Not yet implemented")
    }

    override fun getStateById(id: Long): ResponseEntity<StateDto> {
        TODO("Not yet implemented")
    }

    override fun createState(state: StateDto): ResponseEntity<StateDto> {
        TODO("Not yet implemented")
    }

    override fun updateState(state: StateDto, id: Long): ResponseEntity<StateDto> {
        TODO("Not yet implemented")
    }

    override fun deleteState(id: Long) {
        TODO("Not yet implemented")
    }

    override fun getAllZones(): ResponseEntity<Iterable<ZoneDto>> {
        TODO("Not yet implemented")
    }

    override fun getZoneById(id: Long): ResponseEntity<ZoneDto> {
        TODO("Not yet implemented")
    }

    override fun createZone(zone: ZoneDto): ResponseEntity<ZoneDto> {
        TODO("Not yet implemented")
    }

    override fun updateZone(zone: ZoneDto, id: Long): ResponseEntity<ZoneDto> {
        TODO("Not yet implemented")
    }

    override fun deleteZone(id: Long) {
        TODO("Not yet implemented")
    }
}