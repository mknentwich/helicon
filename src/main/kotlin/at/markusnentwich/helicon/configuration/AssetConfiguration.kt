package at.markusnentwich.helicon.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.DefaultResourceLoader

@Configuration
class AssetConfiguration(
    @Autowired val configurationProperties: HeliconConfigurationProperties
) {

    @Bean
    fun heliconResourceLoader(): HeliconResourceLoader {
        val loader = HeliconResourceLoader()
        loader.addProtocolResolver(AssetResolver(configurationProperties.assets))
        return loader
    }
}

class HeliconResourceLoader : DefaultResourceLoader()
