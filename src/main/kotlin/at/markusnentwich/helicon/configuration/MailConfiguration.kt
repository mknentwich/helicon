package at.markusnentwich.helicon.configuration

import freemarker.cache.TemplateLoader
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import java.io.InputStreamReader
import java.io.Reader
import java.time.ZoneId
import jakarta.annotation.PostConstruct

@Configuration
class MailConfiguration(
    @Autowired val configurationProperties: HeliconConfigurationProperties,
    @Autowired val heliconResourceLoader: HeliconResourceLoader
) {

    private val logger = LoggerFactory.getLogger(MailConfiguration::class.java)

    @Bean
    fun templateConfiguration(): freemarker.template.Configuration {
        val config = freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_30)
        config.templateLoader = ResourceTemplateLoader(heliconResourceLoader)
        config.localizedLookup = false
        return config
    }

    @PostConstruct
    fun logTimezone() {
        logger.info("Helicon uses timezone {}", ZoneId.systemDefault().id)
    }
}

class ResourceTemplateLoader(
    val resourceLoader: HeliconResourceLoader
) : TemplateLoader {

    private val logger = LoggerFactory.getLogger(HeliconResourceLoader::class.java)

    override fun findTemplateSource(name: String?): Any {
        if (name == null) {
            logger.debug("Template name is null")
            return ""
        }
        return resourceLoader.getResource("asset:$name")
    }

    override fun getLastModified(templateSource: Any?): Long {
        if (templateSource is Resource) {
            if (templateSource.exists()) {
                return templateSource.lastModified()
            }
            logger.debug("Template [{}] does not exist", templateSource.filename)
        } else {
            logger.debug("Template [{}] is no Resource", templateSource)
        }
        return -1
    }

    override fun getReader(templateSource: Any?, encoding: String?): Reader {
        if (templateSource is Resource) {
            if (templateSource.exists()) {
                return InputStreamReader(templateSource.inputStream, encoding ?: Charsets.UTF_8.name())
            }
            logger.debug("Template [{}] does not exist", templateSource.filename)
        } else {
            logger.debug("Template [{}] is no Resource", templateSource)
        }
        return Reader.nullReader()
    }

    override fun closeTemplateSource(templateSource: Any?) {
        logger.debug("Try to close resource")
    }
}
