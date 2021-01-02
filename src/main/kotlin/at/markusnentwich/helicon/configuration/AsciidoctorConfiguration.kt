package at.markusnentwich.helicon.configuration

import org.asciidoctor.Asciidoctor
import org.asciidoctor.log.Severity
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AsciidoctorConfiguration {

    private val logger = LoggerFactory.getLogger(AsciidoctorConfiguration::class.java)

    @Bean
    fun asciidoctor(): Asciidoctor {
        val adoc = Asciidoctor.Factory.create()
        adoc.registerLogHandler {
            val level: (String) -> Unit = when (it.severity) {
                Severity.DEBUG -> logger::debug
                Severity.ERROR -> logger::error
                Severity.INFO -> logger::info
                Severity.WARN -> logger::warn
                Severity.UNKNOWN -> logger::warn
                Severity.FATAL -> logger::error
                null -> logger::info
            }
            level(it.message)
        }
        return adoc
    }
}
