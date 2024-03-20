package com.gulftechinnovations.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.gulftechinnovations.data.admin.AdminUserDao
import com.gulftechinnovations.data.user.UserDao
import com.gulftechinnovations.model.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.delay
import java.util.*

fun Routing.adminRoutes(
    application: Application,
    adminUserDao: AdminUserDao,
    userDao:UserDao
) {
    val jwtAudience = application.environment.config.property("jwt.audience").getString()
    val jwtDomain = application.environment.config.property("jwt.domain").getString()
    val jwtSecret = application.environment.config.property("jwt.secret").getString()

    route("/admin") {

        post("/loginAdminUser") {
            try {


                val adminUser = call.receive<AdminUser>()

                println(adminUser)

                val adminUserFromDatabase = adminUserDao.getOneAdminUser(adminUser = adminUser)
                    ?: throw BadRequestException("No Admin User with password ${adminUser.adminPassword}")

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
                val adminUser = call.receive<AdminUser>()


                adminUserDao.updateAdminUser(adminUser = adminUser)

                call.respond("Updated Admin User")
            } catch (e: Exception) {
                call.respond(status = HttpStatusCode.BadRequest, message = e.message ?: "There have problem on request")
            }
        }

        post("/addUser") {
            try {
                val user = call.receive<User>()
                val userId = userDao.insertUser(user = user)
                delay(3000)
                call.respond(userId)
            } catch (e: Exception) {
                call.respond(status = HttpStatusCode.BadRequest, message = e.message ?: "There have problem on request")
            }
        }

        get("/getAllUsers") {
            try {
                val userList = userDao.getAllUsers()
                call.respond(userList)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.ExpectationFailed, e.message ?: "There have some problem")
            }
        }

        put("/updateUser/{oldPassword}") {
            try {
                val oldPassword = call.parameters["oldPassword"]?: throw  Exception("Invalid old password")
                val userToUpdate = call.receive<User>()
                val result = userDao.updateUser(
                    oldPassword = oldPassword,
                    newUser = userToUpdate
                )
                delay(3000)
                call.respond(status = HttpStatusCode.OK, result)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "There have some problem")
            }
        }

        delete("/deleteOneUser/{userPassword}") {
            try {
                val userPassword = call.parameters["userPassword"] ?: throw Exception("Invalid user id parameters")
                userDao.deleteUser(userPassword = userPassword)
                delay(3000)
                call.respond("Deleted")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "There have some problem")
            }
        }
    }
}

