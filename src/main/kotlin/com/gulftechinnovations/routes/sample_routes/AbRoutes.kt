package com.gulftechinnovations.routes.sample_routes

import com.gulftechinnovations.data.sample.AbDao
import com.gulftechinnovations.model.test_samples.Ab
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.abRoutes(
    abDao: AbDao
){
    route("/ab"){
        post("/add") {
            try {
                val ab = call.receive<Ab>()
                abDao.insertAb(ab = ab)
                call.respond("Success")
            }catch (e:Exception){
                call.respond(status = HttpStatusCode.BadRequest, message = e.message?:"There have some problem")
            }
        }
        get("/getAll") {
            try {
                val listOfAb = abDao.getAllAb()
                call.respond(listOfAb)
            }catch (e:Exception){
                call.respond(status = HttpStatusCode.BadRequest, message = e.message?:"There have some problem")
            }
        }
        delete ("/deleteAnAb/{id}"){
            try {
                val id = call.parameters["id"]?.toInt()?:throw Exception("Invalid id")
                abDao.deleteOneAb(id = id)
                call.respond("deleted")
            }catch (e:Exception){
                call.respond(status = HttpStatusCode.BadRequest, message = e.message?:"There have some problem")
            }
        }
    }
}