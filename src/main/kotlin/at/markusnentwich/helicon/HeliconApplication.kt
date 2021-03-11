package at.markusnentwich.helicon

import at.markusnentwich.helicon.repositories.HeliconRepositoryImpl
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(proxyBeanMethods = false)
@ConfigurationPropertiesScan("at.markusnentwich.helicon.configuration")
@EnableJpaRepositories(repositoryBaseClass = HeliconRepositoryImpl::class)
@OpenAPIDefinition(
    info = Info(
        title = "Helicon API",
        version = "1.0",
        description = "This is the backend API for Markus Nentwich",
        license = License(name = "GNU Free Documentation License", url = "https://www.gnu.org/licenses/fdl-1.3.html"),
        contact = Contact(url = "https://github.com/Eiskasten", name = "Richard Stöckl", email = "richard.stoeckl@aon.at")
    )
)
class HeliconApplication

fun main(args: Array<String>) {
    runApplication<HeliconApplication>(*args)
}
