package app.jwtexample.services

import app.jwtexample.config.Param
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import java.util.Date
import org.apache.commons.lang3.time.DateUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class JwtService(
    @Value("\${app.jwt.secret}") private val secret: String
) {
    private val logger = LoggerFactory.getLogger(JwtService::class.java)
    private val algorithm = Algorithm.HMAC512(secret)

    fun verifyToken(token: String): DecodedJWT? {
        val verifier = JWT.require(algorithm).build()
        return verifier.verify(token)
    }

    fun sign(subject: String): String {
        val expirationDateTime = DateUtils.addMinutes(Date(), Param.EXPIRATION_TIME_MIN)
        logger.warn("${Param.LOG_PREFIX}expirationDateTime: $expirationDateTime")

        val token = JWT.create()
            .withIssuer(Param.ISSUER)
            .withExpiresAt(expirationDateTime)
            .withSubject(subject)
            .withClaim("extraInfo", "***pass***")
            .sign(algorithm)
        logger.warn("${Param.LOG_PREFIX}token: $token")
        return token
    }
}