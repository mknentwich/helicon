package at.markusnentwich.helicon.configuration

import org.asciidoctor.Asciidoctor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AsciidoctorConfiguration {

    @Bean
    fun asciidoctor(): Asciidoctor {
        return Asciidoctor.Factory.create()
    }
}
