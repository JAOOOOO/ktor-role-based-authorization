package jaocom.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import jaocom.auth.model.RoleUser

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<AuthenticationException> { call, _ ->
            call.respondRedirect("/login")
        }
        exception<AuthorizationException> { call, cause ->
            call.respond(
                status = HttpStatusCode.Forbidden,
                FreeMarkerContent(
                    "forbidden.ftl",
                    mapOf("userSession" to call.sessions.get<RoleUser>(), "reason" to cause.message)
                )
            )
        }

    }

}

class AuthenticationException(override val message: String? = null) : Throwable()
class AuthorizationException(override val message: String? = null) : Throwable()
