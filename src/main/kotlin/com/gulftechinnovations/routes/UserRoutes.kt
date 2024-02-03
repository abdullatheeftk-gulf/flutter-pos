package com.gulftechinnovations.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.gulftechinnovations.data.user.UserDao
import com.gulftechinnovations.model.User
import com.gulftechinnovations.model.UserResponse
import com.gulftechinnovations.model.UserToUpdate
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.util.*


fun Routing.userRoutes(
    userDao: UserDao,
    application: Application
) {

    val jwtAudience = application.environment.config.property("jwt.audience").getString()
    val jwtDomain = application.environment.config.property("jwt.domain").getString()
    val jwtSecret = application.environment.config.property("jwt.secret").getString()

    /*  authenticate("admin") {*/
    route("/user") {

        post("/registerUser") {
            try {
                val user = call.receive<User>()
                print(user)
                userDao.insertUser(user = user)
                val date = Date()
                date.time += 15 * 60 * 1000
                val token = JWT.create()
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .withClaim("userPassword", user.userPassword)
                    .withExpiresAt(date)
                    .sign(Algorithm.HMAC256(jwtSecret))

                val userResponse = UserResponse(
                    user = user, token = token,
                )

                call.respond(HttpStatusCode.Created, userResponse)
            } catch (e: ExposedSQLException) {
                call.respond(HttpStatusCode.Forbidden, e.message ?: "ExposedSqlException")
            } catch (e: Exception) {
                print(e.message)
                call.respond(
                    HttpStatusCode.BadRequest,
                    e.message ?: "There have some problem while registering user"
                )
            }
        }

        post("/loginUser") {
            try {
                val user = call.receive<User>()

                val userFromDatabase =
                    userDao.getOneUser(userPassword = user.userPassword)
                        ?: throw Exception("User data is not available in database")
                val date = Date()
                date.time += 5 * 60 * 1000
                val token = JWT.create()
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .withClaim("userPassword", userFromDatabase.userPassword)
                    .withExpiresAt(date)
                    .sign(Algorithm.HMAC256(jwtSecret))

                val userResponse = UserResponse(
                    user = userFromDatabase, token = token,
                )

                call.respond(userResponse)


            } catch (e: Exception) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = e.message ?: "There have problem on request"
                )
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

        get("/getOneUserByPassword/{userPassword}") {
            try {
                val userPassword = call.parameters["userPassword"] ?: throw Exception("Invalid user password")

                val user = userDao.getOneUser(userPassword = userPassword) ?: throw Exception("No user found")
                call.respond(status = HttpStatusCode.OK, message = user)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "There have some problem")
            }

        }

        put("/updateUser") {
            try {
                val userToUpdate = call.receive<UserToUpdate>()
                userDao.updateUser(
                    oldPassword = userToUpdate.oldUserPassword,
                    newPassword = userToUpdate.newUserPassword
                )
                call.respond(status = HttpStatusCode.OK, "Updated")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "There have some problem")
            }
        }

        delete("/deleteOneUser/{userPassword}") {
            try {
                val userPassword = call.parameters["userPassword"] ?: throw Exception("Invalid user id parameters")
                userDao.deleteUser(userPassword = userPassword)
                call.respond("Deleted")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "There have some problem")
            }
        }
    }
//}

}