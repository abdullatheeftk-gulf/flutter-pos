package com.gulftechinnovations.routes.dine_in_routes

import com.gulftechinnovations.data.dine_in.area.DineInAreaDao
import com.gulftechinnovations.data.dine_in.table.DineInTableDao
import com.gulftechinnovations.model.dine_in.DineInArea
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.dineInAreaRoutes(
    dineInAreaDao: DineInAreaDao,
    dineInTableDao: DineInTableDao
){

    route("/area"){

        post("/add") {
            try {
                val dineInArea = call.receive<DineInArea>()
                dineInAreaDao.insertArea(area = dineInArea)
                call.respond("Added area")
            }catch (e:CannotTransformContentToTypeException){
                call.respond(status = HttpStatusCode.BadRequest,"Json Convert Exception")
            }
            catch (e:Exception){
                call.respond(status = HttpStatusCode.ExpectationFailed,e.message?:"There have problem while adding area")
            }
        }

        get ("/getAllAreas"){
            try{
                val dineInAreas = dineInAreaDao.getAllArea()
                call.respond(dineInAreas)
            }catch (e:Exception){
                call.respond(status = HttpStatusCode.BadRequest,e.message?:"There have problem while getting all area")
            }
        }

        get ("/getAllTablesUnderAnArea/{areaId}"){
            try {
                val areaId = call.parameters["areaId"]?.toInt()?:throw BadRequestException("Invalid area")
                val dineInTables = dineInTableDao.getTablesByAreaId(areaId = areaId)
                call.respond(dineInTables)
            }catch (e:Exception){
                call.respond(status = HttpStatusCode.BadRequest,e.message?:"There have problem while getting all area")
            }
        }

        put("/updateAnArea") {
            try {
                val dineInArea = call.receive<DineInArea>()
                dineInAreaDao.updateArea(dineInArea = dineInArea)
                call.respond("Updated")
            }catch (e:Exception){
                call.respond(status = HttpStatusCode.BadRequest,e.message?:"There have problem while updating all area")
            }
        }

        delete("/deleteAnAreaById/{areaId}") {
            try {
                val id = call.parameters["areaId"]?.toInt()?:throw Exception("Invalid area id")
                dineInAreaDao.deleteAreaById(id = id)
                call.respond("Deleted")
            }catch (e:Exception){
                call.respond(status = HttpStatusCode.BadRequest,e.message?:"There have problem while updating all area")
            }
        }

        delete("/deleteAllAreas") {
            try {
                dineInAreaDao.deleteAllArea()
                call.respond("Deleted All areas")
            }catch (e:Exception){
                call.respond(status = HttpStatusCode.BadRequest,e.message?:"There have problem while updating all area")
            }
        }
    }



}