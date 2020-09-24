package at.markusnentwich.helicon

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HeliconApplication

fun main(args: Array<String>) {
    runApplication<HeliconApplication>(*args)
}
