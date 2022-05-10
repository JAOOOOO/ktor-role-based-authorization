package jaocom.plugins

import freemarker.cache.*
import io.ktor.server.freemarker.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import jaocom.auth.model.RoleUser

fun Application.configureTemplating() {
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }
}


suspend fun ApplicationCall.showContent(template: String) = respond(
    FreeMarkerContent(
        template,
        mapOf("userSession" to sessions.get<RoleUser>())
    )
)


suspend fun ApplicationCall.showProtectedContent(template: String, restriction: String) = respond(
    FreeMarkerContent(
        template,
        mapOf("userSession" to sessions.get<RoleUser>(), "restriction" to restriction)
    )
)

