package com.gulftechinnovations.routes.dine_in_routes

import com.gulftechinnovations.data.dine_in.area.DineInAreaDao
import com.gulftechinnovations.data.dine_in.table.DineInTableDao
import com.gulftechinnovations.model.dine_in.DineInTable
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.dineInTableRoutes(
    dineInTableDao: DineInTableDao
){
    route("/table"){

        post ("/add"){
            try {
                val dineInTable = call.receive<DineInTable>()
                dineInTableDao.insetTable(dineInTable = dineInTable)
                call.respond("Added table")
            }catch (e:Exception){
                call.respond(status = HttpStatusCode.BadRequest, message = e.message?:"There have while adding table")
            }
        }

        get ("/getATableById/{id}"){
            try {
                val id = call.parameters["id"]?.toInt()?:throw Exception("Invalid table id")
                val dineInTable = dineInTableDao.getATableById(id)?:throw  Exception("No Table with with this $id")
                call.respond(dineInTable)
            }catch (e:Exception){
                call.respond(status = HttpStatusCode.BadRequest, message = e.message?:"There have while adding table")
            }
        }
        put("/updateATable") {
            try{
                val dineInTable = call.receive<DineInTable>()
                dineInTableDao.updateATable(dineInTable = dineInTable)
                call.respond("Updated")
            }catch (e:Exception){
                call.respond(status = HttpStatusCode.BadRequest, message = e.message?:"There have while adding table")
            }
        }
        delete ("/deleteATableById/{id}"){
           try {
               val id = call.parameters["id"]?.toInt()?:throw  Exception("Invalid id parameters")
               dineInTableDao.deleteATable(id = id)
               call.respond("deleted")
           } catch (e:Exception){
               call.respond(status = HttpStatusCode.BadRequest, message = e.message?:"There have while adding table")
           }
        }

    }
}