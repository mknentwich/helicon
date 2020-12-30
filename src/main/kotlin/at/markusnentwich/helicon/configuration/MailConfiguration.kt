package at.markusnentwich.helicon.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File

@Configuration
class MailConfiguration(
    @Autowired val configurationProperties: HeliconConfigurationProperties
) {

    @Bean
    fun templateConfiguration(): freemarker.template.Configuration {
        val config = freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_30)
        config.setDirectoryForTemplateLoading(File(configurationProperties.assets))
        return config
    }
}
