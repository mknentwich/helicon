package at.markusnentwich.helicon.configuration

import org.slf4j.LoggerFactory
import org.springframework.core.io.ProtocolResolver
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import java.nio.file.Path

class AssetResolver(private val assetFileSystemPath: String) : ProtocolResolver {
    companion object {
        const val PREFIX = "asset"
    }

    private val logger = LoggerFactory.getLogger(AssetResolver::class.java)

    override fun resolve(location: String, resourceLoader: ResourceLoader): Resource? {
        if (!location.startsWith("$PREFIX:")) {
            return null
        }
        logger.debug("Try to resolve resource [{}]", location)
        val resourceName = location.removePrefix("$PREFIX:")
        val filesystemResource = resourceLoader.getResource("file:${Path.of(assetFileSystemPath, resourceName)}")
        if (filesystemResource.exists()) {
            logger.debug("Resource [{}] exists in filesystem", location)
            return filesystemResource
        }
        logger.debug("Resource [{}] does not exist in filesystem, returning from classpath", location)
        return resourceLoader.getResource("classpath:assets/$resourceName")
    }
}
