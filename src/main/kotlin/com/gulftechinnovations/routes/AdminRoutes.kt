package com.gulftechinnovations.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.gulftechinnovations.model.AdminUser
import com.gulftechinnovations.data.admin.AdminUserDao
import com.gulftechinnovations.model.AdminPasswordToReset
import com.gulftechinnovations.model.AdminResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
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

                println(adminUser)

                val adminUserFromDatabase = adminUserDao.getOneAdminUserByName(adminUser = adminUser)
                    ?: throw Exception("Admin User is not exist")

                val date = Date()
                date.time += 15 * 60 * 1000
                val token = JWT.create()
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .withClaim("adminName", adminUserFromDatabase.adminName)
                    .withExpiresAt(date)
                    .sign(Algorithm.HMAC256(jwtSecret))

                val adminResponse = AdminResponse(
                    adminUserFromDatabase, token
                )

                call.respond(adminResponse)

            } catch (e: Exception) {
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

                val adminResponse = AdminResponse(
                    adminUser, token
                )

                call.respond(HttpStatusCode.Created, adminResponse)

            } catch (e: Exception) {
                call.respond(status = HttpStatusCode.BadRequest, message = e.message ?: "There have problem on request")
            }
        }


        put("/updateAdminUser") {
            try {
                val adminPasswordToReset = call.receive<AdminPasswordToReset>()

                val newAdminReceived = adminPasswordToReset.newAdminUser
                val oldAdminReceived = adminPasswordToReset.oldAdminUser

                val oldAdminUser = adminUserDao.getOneAdminUser(adminPassword = oldAdminReceived.adminPassword)
                    ?: throw Exception("No admin user with this password")

                val newAdminUser = oldAdminUser.copy(
                    adminId = oldAdminUser.adminId,
                    adminName = newAdminReceived.adminName,
                    adminPassword = newAdminReceived.adminPassword,
                    licenseKey = newAdminReceived.licenseKey,
                    isLicenceKeyVerified = newAdminReceived.isLicenceKeyVerified
                )

                adminUserDao.updateAdminUser(adminUser = newAdminUser)


                call.respond("Updated Admin User")


            } catch (e: Exception) {
                call.respond(status = HttpStatusCode.BadRequest, message = e.message ?: "There have problem on request")
            }
        }
    }
}

