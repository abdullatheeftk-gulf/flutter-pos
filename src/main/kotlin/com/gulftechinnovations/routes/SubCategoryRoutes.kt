package com.gulftechinnovations.routes

import com.gulftechinnovations.data.sub_category.SubCategoryDao
import com.gulftechinnovations.model.SubCategory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.subCategoryRoutes(
    subCategoryDao: SubCategoryDao
){

    route("/subCategory"){

        post("/addASubCategory"){
            try {
                val subCategory = call.receive<SubCategory>()
                val value = subCategoryDao.insertSubCategory(subCategory = subCategory)
                call.respond(HttpStatusCode.Created,value)
            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,e.message?:"There have some problem")
            }
        }

        put ("/updateASubCategory"){
            try {
                val subCategory = call.receive<SubCategory>()
                subCategoryDao.updateASubCategory(subCategory = subCategory)
                call.respondText("updated")
            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,e.message?:"There have some problem")
            }
        }
        delete("/deleteASubCategory/{id}") {
            try {
                val id = call.parameters["id"]?.toInt()?:throw Exception("No Id parameter")
                subCategoryDao.deleteASubCategory(subCategoryId = id)
                call.respondText("deleted")
            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,e.message?:"There have some problem")
            }
        }
    }
}