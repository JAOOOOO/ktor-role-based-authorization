package jaocom.plugins

import io.ktor.server.routing.*
import io.ktor.server.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import jaocom.auth.model.OriginalRequestURI
import jaocom.auth.model.RoleUser
import jaocom.auth.withAnyRole
import jaocom.auth.withRoles
import jaocom.auth.withoutRoles

fun Application.configureRouting() {


    routing {

        get("/") { call.showContent("home.ftl") }
        get("/login") { call.showContent("login.ftl") }

        post("/login") {
            val params = call.receiveParameters()
            val username = params["username"]
            val password = params["password"]
            val roles = params.getAll("roles")?.toSet() ?: emptySet()
            if (username != null && !password.isNullOrEmpty()) {
                call.sessions.set(RoleUser(username, roles))
                val redirectURL = call.sessions.get<OriginalRequestURI>()?.also {
                    call.sessions.clear<OriginalRequestURI>()
                }
                call.respondRedirect(redirectURL?.uri ?: "/")
            } else {
                call.respondRedirect("/login")
            }
        }

        get("/logout") {
            call.sessions.clear<RoleUser>()
            call.respondRedirect("/")
        }

        authenticate {
            get("/login-required") {
                call.showProtectedContent("protected.ftl", "Principal must exist")
            }
            withRoles("ABC") {
                get("/role-abc-required") {
                    call.showProtectedContent("protected.ftl", "Principal must have role ABC")
                }
            }
            withRoles("ABC", "DEF") {
                get("/roles-abc-def-required") {
                    call.showProtectedContent("protected.ftl", "Principal must have roles ABC and DEF")
                }
            }
            withAnyRole("DEF", "GHI") {
                get("/roles-any-from-def-ghi-required") {
                    call.showProtectedContent(
                        "protected.ftl", "Principal must have role DEF and/or GHI"
                    )
                }
            }
            withoutRoles("ABC", "GHI") {
                get("/roles-none-of-abc-ghi-allowed") {
                    call.showProtectedContent(
                        "protected.ftl", "Principal must NOT have roles ABC or GHI"
                    )
                }
            }
            withRoles("ABC") {
                withoutRoles("GHI") {
                    get("/role-abc-required-ghi-forbidden") {
                        call.showProtectedContent(
                            "protected.ftl", "Principal must have role ABC and must NOT have role GHI"
                        )
                    }
                }
            }
        }

        static("static") {
            resources("static")
        }

    }

}

