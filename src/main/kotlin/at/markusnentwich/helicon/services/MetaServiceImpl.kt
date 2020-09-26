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

    override fun getStateById(id: Int): ResponseEntity<StateDto> {
        TODO("Not yet implemented")
    }

    override fun createState(state: StateDto): ResponseEntity<StateDto> {
        TODO("Not yet implemented")
    }

    override fun updateState(state: StateDto, id: Int): ResponseEntity<StateDto> {
        TODO("Not yet implemented")
    }

    override fun deleteState(id: Int) {
        TODO("Not yet implemented")
    }

    override fun getAllZones(): ResponseEntity<Iterable<ZoneDto>> {
        TODO("Not yet implemented")
    }

    override fun getZoneById(id: Int): ResponseEntity<ZoneDto> {
        TODO("Not yet implemented")
    }

    override fun createZone(zone: ZoneDto): ResponseEntity<ZoneDto> {
        TODO("Not yet implemented")
    }

    override fun updateZone(zone: ZoneDto, id: Int): ResponseEntity<ZoneDto> {
        TODO("Not yet implemented")
    }

    override fun deleteZone(id: Int) {
        TODO("Not yet implemented")
    }
}