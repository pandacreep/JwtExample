package app.jwtexample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JwtExampleApplication

fun main(args: Array<String>) {
    runApplication<JwtExampleApplication>(*args)
}
