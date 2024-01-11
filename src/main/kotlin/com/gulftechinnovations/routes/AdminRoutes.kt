package com.gulftechinnovations.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.gulftechinnovations.model.AdminUser
import com.gulftechinnovations.data.admin.AdminUserDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Routing.adminRoutes(
    application: Application,
    adminUserDao: AdminUserDao
) {
    val jwtAudience = application.environment.config.property("jwt.audience").getString()
    val jwtDomain = application.environment.config.property("jwt.domain").getString()
    val jwtSecret = application.environment.config.property("jwt.secret").getString()

    route("/admin") {

        post("/loginAdminUser") {
            try {
                val adminUser = call.receive<AdminUser>()
                val headersSample = call.request.header(name = "sample")
                println("===================== $headersSample")
                println(adminUser)
                val adminUserFromDatabase = adminUserDao.getOneAdminUserByName(adminUser = adminUser)
                    ?: throw Exception("User is not exist")

                val date = Date()
                date.time += 15 * 60 * 1000
                val token = JWT.create()
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .withClaim("adminName", adminUserFromDatabase.adminName)
                    .withExpiresAt(date)
                    .sign(Algorithm.HMAC256(jwtSecret))

                call.respond(hashMapOf("token" to token))

            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(status = HttpStatusCode.BadRequest, message = e.message ?: "There have problem on request")
            }
        }

        post("/registerAdminUser") {
            try {
                val adminUser = call.receive<AdminUser>()
                adminUserDao.insertAdminUser(adminUser = adminUser)

                val date = Date()
                date.time += 15 * 60 * 1000
                val token = JWT.create()
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .withClaim("adminName", adminUser.adminName)
                    .withExpiresAt(date)
                    .sign(Algorithm.HMAC256(jwtSecret))

                call.respond(HttpStatusCode.Created,hashMapOf("token" to token))

            } catch (e: Exception) {
                call.respond(status = HttpStatusCode.BadRequest, message = e.message ?: "There have problem on request")
            }
        }
    }
}