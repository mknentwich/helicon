package at.markusnentwich.helicon.services

import at.markusnentwich.helicon.controller.*
import at.markusnentwich.helicon.dto.StateDto
import at.markusnentwich.helicon.dto.ZoneDto
import at.markusnentwich.helicon.repositories.OrderRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class MetaServiceImpl(
    @Autowired val metaController: MetaController,
    @Autowired val repo: OrderRepository
) : MetaService {
    val logger = LoggerFactory.getLogger(MetaServiceImpl::class.java)

    override fun getAllStates(): ResponseEntity<Iterable<StateDto>> {
        callLog("getAllStates")
        return ResponseEntity.ok(metaController.getAllStates())
    }

    override fun getStateById(id: Long): ResponseEntity<StateDto> {
        callLog("getStateById")
        val status: HttpStatus = try {
            return ResponseEntity.ok(metaController.getStateById(id))
        } catch (e: NotFoundException) {
            HttpStatus.NOT_FOUND
        }
        return ResponseEntity.status(status).build()
    }

    override fun createState(state: StateDto, jwt: String): ResponseEntity<StateDto> {
        callLog("createState")
        val status = try {
            return ResponseEntity.ok(metaController.createState(state, jwt))
        } catch (e: BadPayloadException) {
            HttpStatus.BAD_REQUEST
        }
        return ResponseEntity.status(status).build()
    }

    override fun updateState(state: StateDto, id: Long, jwt: String): ResponseEntity<StateDto> {
        callLog("updateState")
        val status = try {
            return ResponseEntity.ok(metaController.updateState(state, id, jwt))
        } catch (e: NotFoundException) {
            HttpStatus.NOT_FOUND
        } catch (e: BadPayloadException) {
            HttpStatus.BAD_REQUEST
        } catch (e: AlreadyExistsException) {
            HttpStatus.CONFLICT
        }
        return ResponseEntity.status(status).build()
    }

    override fun deleteState(id: Long, jwt: String): ResponseEntity<Void> {
        callLog("deleteState")
        val status = try {
            metaController.deleteState(id, jwt)
            HttpStatus.OK
        } catch (e: NotFoundException) {
            HttpStatus.NOT_FOUND
        }
        return ResponseEntity.status(status).build()
    }

    override fun getAllZones(): ResponseEntity<Iterable<ZoneDto>> {
        callLog("getAllZones")
        return ResponseEntity.ok(metaController.getAllZones())
    }

    override fun getZoneById(id: Long): ResponseEntity<ZoneDto> {
        callLog("getZoneById")
        val status: HttpStatus = try {
            return ResponseEntity.ok(metaController.getZoneById(id))
        } catch (e: NotFoundException) {
            HttpStatus.NOT_FOUND
        }
        return ResponseEntity.status(status).build()
    }

    override fun createZone(zone: ZoneDto, jwt: String): ResponseEntity<ZoneDto> {
        callLog("createZone")
        val status: HttpStatus = try {
            return ResponseEntity.ok(metaController.createZone(zone, jwt))
        } catch (e: JWTFormatViolationException) {
            HttpStatus.BAD_REQUEST
        }
        return ResponseEntity.status(status).build()
    }

    override fun updateZone(zone: ZoneDto, id: Long, jwt: String): ResponseEntity<ZoneDto> {
        callLog("updateZone")
        val status = try {
            return ResponseEntity.ok(metaController.updateZone(zone, id, jwt))
        } catch (e: NotFoundException) {
            HttpStatus.NOT_FOUND
        } catch (e: BadPayloadException) {
            HttpStatus.BAD_REQUEST
        } catch (e: AlreadyExistsException) {
            HttpStatus.CONFLICT
        }
        return ResponseEntity.status(status).build()
    }

    override fun deleteZone(id: Long, jwt: String): ResponseEntity<Void> {
        callLog("deleteZone")
        val status = try {
            metaController.deleteState(id, jwt)
            HttpStatus.OK
        } catch (e: NotFoundException) {
            HttpStatus.NOT_FOUND
        }
        return ResponseEntity.status(status).build()
    }

    private fun callLog(method: String) {
        logger.trace("called {}", method)
    }
}
