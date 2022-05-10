package jaocom.auth.model

import io.ktor.server.auth.*
import jaocom.auth.Role

data class RoleUser(val name: String, val roles: Set<Role>) : Principal
