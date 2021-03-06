package at.markusnentwich.helicon.controller

import at.markusnentwich.helicon.dto.StateDto
import at.markusnentwich.helicon.dto.ZoneDto
import at.markusnentwich.helicon.entities.StateEntity
import at.markusnentwich.helicon.entities.ZoneEntity
import at.markusnentwich.helicon.repositories.StateRepository
import at.markusnentwich.helicon.repositories.ZoneRepository
import org.modelmapper.ModelMapper
import org.modelmapper.TypeToken
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import java.lang.reflect.Type

@Controller
class MetaControllerImpl(
    @Autowired val stateRepository: StateRepository,
    @Autowired val zoneRepository: ZoneRepository,
    @Autowired val modelMapper: ModelMapper
) : MetaController {
    val logger = LoggerFactory.getLogger(MetaControllerImpl::class.java)

    override fun getAllStates(): List<StateDto> {
        val listType: Type = object : TypeToken<List<StateDto>>() {}.type
        return modelMapper.map(stateRepository.findAll(), listType)
    }

    override fun getStateById(id: Long): StateDto {
        if (!stateRepository.existsById(id)) {
            logger.error("no state with id {} found", id)
            throw NotFoundException()
        }
        return modelMapper.map(stateRepository.findById(id).get(), StateDto::class.java)
    }

    override fun createState(state: StateDto, jwt: String): StateDto {
        if (state.id != null) {
            logger.error("state id must not be provided")
            throw BadPayloadException()
        }
        val zoneEntity = zoneRepository.findByIdOrNull(state.zoneId!!)
        if (zoneEntity == null) {
            logger.error("zone with id {} does not exist", state.zone.id)
            throw BadPayloadException()
        }
        val stateEntity: StateEntity = modelMapper.map(state, StateEntity::class.java)
        stateEntity.zone = zoneEntity
        return modelMapper.map(stateRepository.save(stateEntity), StateDto::class.java)
    }

    override fun updateState(state: StateDto, id: Long, jwt: String): StateDto {
        if (!stateRepository.existsById(id)) {
            logger.error("state with id {} does not exist", id)
            throw NotFoundException()
        }
        if (state.id != null && state.id != id) {
            logger.error("Changing the state id is not supported")
            throw AlreadyExistsException()
        }
        val stateEntity = modelMapper.map(state, StateEntity::class.java)
        val dto = modelMapper.map(stateRepository.save(stateEntity), StateDto::class.java)
        dto.zone = modelMapper.map(zoneRepository.findById(id), ZoneDto::class.java)
        return dto
    }

    override fun deleteState(id: Long, jwt: String) {
        if (!stateRepository.existsById(id)) {
            logger.error("state with id {} does not exist", id)
            throw NotFoundException()
        }
        stateRepository.deleteById(id)
    }

    override fun getAllZones(): List<ZoneDto> {
        val listType: Type = object : TypeToken<List<ZoneDto>>() {}.type
        return modelMapper.map(zoneRepository.findAll(), listType)
    }

    override fun getZoneById(id: Long): ZoneDto {
        if (!zoneRepository.existsById(id)) {
            logger.error("zone with id {} does not exist", id)
            throw NotFoundException()
        }
        return modelMapper.map(zoneRepository.findById(id), ZoneDto::class.java)
    }

    override fun createZone(zone: ZoneDto, jwt: String): ZoneDto {
        if (zone.id != null) {
            logger.error("zone id must not be provided")
            throw BadPayloadException()
        }
        val zoneEntity = modelMapper.map(zone, ZoneEntity::class.java)
        return modelMapper.map(zoneRepository.save(zoneEntity), ZoneDto::class.java)
    }

    override fun updateZone(zone: ZoneDto, id: Long, jwt: String): ZoneDto {
        if (!zoneRepository.existsById(id)) {
            logger.error("zone with id {} does not exist", id)
        }
        if (zone.id != null && zone.id != id) {
            logger.error("Changing the zone id is not supported")
            throw BadPayloadException()
        }
        val zoneEntity = modelMapper.map(zone, ZoneEntity::class.java)
        return modelMapper.map(zoneRepository.save(zoneEntity), ZoneDto::class.java)
    }

    override fun deleteZone(id: Long, jwt: String) {
        if (!zoneRepository.existsById(id)) {
            logger.error("zone with id {} does not exist", id)
            throw NotFoundException()
        }
        zoneRepository.deleteById(id)
    }
}
