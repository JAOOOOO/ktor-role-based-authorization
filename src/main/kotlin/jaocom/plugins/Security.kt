package jaocom.plugins

import io.ktor.server.auth.*
import io.ktor.util.*
import io.ktor.server.sessions.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import jaocom.auth.model.OriginalRequestURI
import jaocom.auth.model.RoleUser

fun Application.configureSecurity() {
    install(Sessions) {
        cookie<RoleUser>("user_session_cookie", SessionStorageMemory())
        cookie<OriginalRequestURI>("original_request_cookie")
    }

    authentication {
        session<RoleUser> {
            challenge {
                call.sessions.set(OriginalRequestURI(call.request.uri))
                call.respondRedirect("/login")
            }
            validate {
                it
            }
        }

    }
}
