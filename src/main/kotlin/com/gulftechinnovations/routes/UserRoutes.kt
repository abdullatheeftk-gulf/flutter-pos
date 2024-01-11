package com.gulftechinnovations.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.gulftechinnovations.data.user.UserDao
import com.gulftechinnovations.model.User
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

    authenticate("admin") {
        route("/user") {

            post("/registerUser") {
                try {
                    val user = call.receive<User>()
                    userDao.insertUser(user = user)

                    val date = Date()
                    date.time += 15 * 60 * 1000
                    val token = JWT.create()
                        .withAudience(jwtAudience)
                        .withIssuer(jwtDomain)
                        .withClaim("username", user.userName)
                        .withExpiresAt(date)
                        .sign(Algorithm.HMAC256(jwtSecret))

                    call.respond(HttpStatusCode.Created,hashMapOf("token" to token))
                } catch (e: ExposedSQLException) {
                    call.respond(HttpStatusCode.Forbidden, e.message ?: "ExposedSqlException")
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        e.message ?: "There have some problem while registering user"
                    )
                }
            }

            post("/loginUser") {
                try {
                    val user = call.receive<User>()
                    val userFromDatabase = userDao.getOneUser(userId = user.userId) ?: throw Exception("No data in database")
                    val date = Date()
                    date.time += 5 * 60 * 1000
                    val token = JWT.create()
                        .withAudience(jwtAudience)
                        .withIssuer(jwtDomain)
                        .withClaim("username", userFromDatabase.userName)
                        .withExpiresAt(date)
                        .sign(Algorithm.HMAC256(jwtSecret))



                    call.respond(hashMapOf("token" to token))


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
                }catch (e:Exception){
                    call.respond(HttpStatusCode.ExpectationFailed,e.message?:"There have some problem")
                }
            }

            get("/getOneUser/{id}") {
                try {
                    val id = call.parameters["id"]?.toInt() ?: throw  Exception("Invalid user id parameters")

                    val user = userDao.getOneUser(id)
                    call.respond(status = HttpStatusCode.OK, message = user ?: "late")
                }catch (e:Exception){
                    call.respond(HttpStatusCode.BadRequest,e.message?:"There have some problem")
                }

            }

            put("/updateUser") {
                try {
                    val user = call.receive<User>()
                    val id = userDao.updateUser(user = user)
                    call.respond(status = HttpStatusCode.OK, hashMapOf("id" to id))
                }catch (e:Exception){
                    call.respond(HttpStatusCode.BadRequest,e.message?:"There have some problem")
                }
            }

            delete("/deleteOneUser/{id}") {
                try {
                    val id = call.parameters["id"]?.toInt() ?: throw Exception("Invalid user id parameters")
                    userDao.deleteUser(userId = id)
                    call.respond("Deleted")
                }catch (e:Exception){
                    call.respond(HttpStatusCode.BadRequest,e.message?:"There have some problem")
                }
            }
        }
    }
}