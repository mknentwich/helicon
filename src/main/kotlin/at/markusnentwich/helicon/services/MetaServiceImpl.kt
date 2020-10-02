package at.markusnentwich.helicon.services

import at.markusnentwich.helicon.controller.JWTFormatViolationException
import at.markusnentwich.helicon.controller.MetaController
import at.markusnentwich.helicon.controller.NotFoundException
import at.markusnentwich.helicon.dto.StateDto
import at.markusnentwich.helicon.dto.ZoneDto
import at.markusnentwich.helicon.repositories.OrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class MetaServiceImpl(
        @Autowired val metaController: MetaController,
        @Autowired val repo: OrderRepository
) : MetaService {
    override fun getAllStates(): ResponseEntity<Iterable<StateDto>> {
        return ResponseEntity.ok(metaController.getAllStates())
    }

    override fun getStateById(id: Long): ResponseEntity<StateDto> {
        val status: HttpStatus = try {
            return ResponseEntity.ok(metaController.getStateById(id))
        } catch (e: NotFoundException) {
            HttpStatus.NOT_FOUND
        }
        return ResponseEntity.status(status).build()
    }

    override fun createState(state: StateDto, jwt: String): ResponseEntity<StateDto> {
        TODO("Not yet implemented")
    }

    override fun updateState(state: StateDto, id: Long, jwt: String): ResponseEntity<StateDto> {
        TODO("Not yet implemented")
    }

    override fun deleteState(id: Long, jwt: String) {
        TODO("Not yet implemented")
    }

    override fun getAllZones(): ResponseEntity<Iterable<ZoneDto>> {
        return ResponseEntity.ok(metaController.getAllZones())
    }

    override fun getZoneById(id: Long): ResponseEntity<ZoneDto> {
        val status: HttpStatus = try {
            return ResponseEntity.ok(metaController.getZoneById(id))
        } catch (e: NotFoundException) {
            HttpStatus.NOT_FOUND
        }
        return ResponseEntity.status(status).build()
    }

    override fun createZone(zone: ZoneDto, jwt: String): ResponseEntity<ZoneDto> {
        val status: HttpStatus = try {
            return ResponseEntity.ok(metaController.createZone(zone, jwt))
        } catch (e: JWTFormatViolationException) {
            HttpStatus.BAD_REQUEST
        }
        return ResponseEntity.status(status).build()
    }

    override fun updateZone(zone: ZoneDto, id: Long, jwt: String): ResponseEntity<ZoneDto> {
        TODO("Not yet implemented")
    }

    override fun deleteZone(id: Long, jwt: String) {
        TODO("Not yet implemented")
    }
}