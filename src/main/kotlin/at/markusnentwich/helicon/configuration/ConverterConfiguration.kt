package at.markusnentwich.helicon.configuration

import at.markusnentwich.helicon.controller.BadPayloadException
import at.markusnentwich.helicon.dto.*
import at.markusnentwich.helicon.entities.*
import org.modelmapper.Converter
import org.modelmapper.ModelMapper
import org.modelmapper.spi.MappingContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ConverterConfiguration {

    @Bean
    fun modelMapper(): ModelMapper {
        val m = ModelMapper()
        m.addConverter(AddressConverter())
        m.addConverter(IdentityConverter())
        m.addConverter(OrderConverter())
        m.addConverter(StateConverter())
        m.addConverter(ZoneConverter())
        return m
    }
}

class ZoneConverter : Converter<ZoneDto, ZoneEntity> {
    override fun convert(context: MappingContext<ZoneDto, ZoneEntity>?): ZoneEntity {
        if (context == null)
            throw BadPayloadException()
        return ZoneEntity(name = context.source.name, shipping = context.source.shipping)
    }
}

class StateConverter : Converter<StateDto, StateEntity> {
    override fun convert(context: MappingContext<StateDto, StateEntity>?): StateEntity {
        if (context == null)
            throw BadPayloadException()
        return StateEntity(name = context.source.name, zone = ZoneEntity(id = context.source.zone.id, name = "unnamed", shipping = 1))
    }
}

class AddressConverter : Converter<AddressDto, AddressEntity> {
    override fun convert(context: MappingContext<AddressDto, AddressEntity>?): AddressEntity {
        if (context == null)
            throw BadPayloadException()
        val dto = context.source
        return AddressEntity(city = dto.city, postCode = dto.postCode, street = dto.street, streetNumber = dto.streetNumber, state = StateEntity(id = dto.state.id))
    }
}

class IdentityConverter : Converter<IdentityDto, IdentityEntity> {
    override fun convert(context: MappingContext<IdentityDto, IdentityEntity>?): IdentityEntity {
        if (context == null)
            throw BadPayloadException()
        val dto = context.source
        return IdentityEntity(firstName = dto.firstName, lastName = dto.lastName, company = dto.company, email = dto.email, telephone = dto.telephone, address = AddressEntity(id = dto.address.id, state = StateEntity()))
    }
}

class OrderConverter : Converter<ScoreOrderDto, OrderEntity> {
    override fun convert(context: MappingContext<ScoreOrderDto, OrderEntity>?): OrderEntity {
        if (context == null)
            throw BadPayloadException()
        val dto = context.source
        return OrderEntity(
            deliveryAddress = AddressEntity(id = dto.deliveryAddress?.id),
            identity = IdentityEntity(id = dto.identity.id)
        )
    }
}
