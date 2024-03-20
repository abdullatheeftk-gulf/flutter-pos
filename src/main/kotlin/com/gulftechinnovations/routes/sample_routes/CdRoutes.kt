package com.gulftechinnovations.routes.sample_routes

import com.gulftechinnovations.data.sample.CdDao
import com.gulftechinnovations.model.test_samples.Cd
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.cdRoutes(
    cdDao: CdDao
) {
    route("/cd") {
        post("/add") {
            try {
                val cd = call.receive<Cd>()
                cdDao.insertCd(cd = cd)
                call.respond(message = "Added")
            } catch (e: Exception) {
                call.respond(status = HttpStatusCode.BadRequest, message = e.message ?: "There have some problem")
            }
        }
        get("/getAll") {
            try {
                val listOfCd = cdDao.getAllCd()
                call.respond(listOfCd)
            } catch (e: Exception) {
                call.respond(status = HttpStatusCode.BadRequest, message = e.message ?: "There have some problem")
            }
        }
        delete("/deleteOneCd/{id}") {
            try {
                val id = call.parameters["id"]?.toInt() ?: throw Exception("Invalid id")
                cdDao.deleteACd(id)
                call.respond("Deleted")
            } catch (e: Exception) {
                call.respond(status = HttpStatusCode.BadRequest, message = e.message ?: "There have some problem")
            }
        }
    }
}

