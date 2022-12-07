package app.jwtexample.web.controllers

import app.jwtexample.services.JwtService
import java.time.LocalDateTime
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class MainController(
    private val jwtService: JwtService
) {

    @GetMapping("/hello")
    fun hello() = "Hello! System time: ${LocalDateTime.now()}"

    @GetMapping("/hello-restricted")
    fun helloRestricted() = "Hello (restricted)! System time: ${LocalDateTime.now()}"

    @GetMapping("/get-jwt")
    fun getJwt() = jwtService.sign("user")
}