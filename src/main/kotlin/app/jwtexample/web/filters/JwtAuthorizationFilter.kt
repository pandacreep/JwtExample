package app.jwtexample.web.filters

import app.jwtexample.config.Param
import app.jwtexample.services.JwtService
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component

@Component
class JwtAuthorizationFilter(
    private val jwtService: JwtService
) : Filter {
    private val logger = LoggerFactory.getLogger(JwtAuthorizationFilter::class.java)

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val req = request as HttpServletRequest

        if (exceptionRoutes.any { req.servletPath.matches(it.toRegex()) }) {
            chain.doFilter(request, response)
            return
        }
        val headerAuthorization = req.getHeader(HttpHeaders.AUTHORIZATION)
        logger.warn("${Param.LOG_PREFIX}HeaderAuthorization: $headerAuthorization")
        if (headerAuthorization === null || !headerAuthorization.startsWith(TOKEN_PREFIX)) {
            throw RuntimeException("BEARER_TOKEN_NOT_SET")
        }

        val token = headerAuthorization.replace(TOKEN_PREFIX, "")
        val decodedJwt = jwtService.verifyToken(token)
        logger.warn("${Param.LOG_PREFIX}decodedJwt.id: ${decodedJwt?.id}")
        logger.warn("${Param.LOG_PREFIX}decodedJwt.subject: ${decodedJwt?.subject}")
        logger.warn("${Param.LOG_PREFIX}decodedJwt.claims: ${decodedJwt?.claims}")
        logger.warn("${Param.LOG_PREFIX}decodedJwt.claims.extraInfo: ${decodedJwt?.claims?.get("extraInfo")}")
        logger.warn("${Param.LOG_PREFIX}decodedJwt.token: ${decodedJwt?.token}")
        logger.warn("${Param.LOG_PREFIX}decodedJwt.header: ${decodedJwt?.header}")
        logger.warn("${Param.LOG_PREFIX}decodedJwt.payload: ${decodedJwt?.payload}")
        logger.warn("${Param.LOG_PREFIX}decodedJwt.signature: ${decodedJwt?.signature}")

        chain.doFilter(request, response)
    }

    private val exceptionRoutes = setOf(
        "/api/hello",
        "/api/health",
        "/api/get-jwt",
    )

    private companion object {
        const val TOKEN_PREFIX = "Bearer "
    }
}