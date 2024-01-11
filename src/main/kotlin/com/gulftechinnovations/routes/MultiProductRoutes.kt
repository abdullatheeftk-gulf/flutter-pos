package com.gulftechinnovations.routes

import com.gulftechinnovations.data.multi_product.MultiProductDao
import com.gulftechinnovations.model.MultiProduct
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.multiProductRoutes(
    multiProductDao: MultiProductDao
){
    route("/multiProduct") {
        post("/addAmuMultiProduct") {
            try {
                val multiProducts = call.receive<MultiProduct>()
                val value = multiProductDao.insertMultiProduct(multiProduct = multiProducts)
                call.respond(value)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "There have some problem")
            }
        }

        get("getAMultiProductByParentId/{parentId}") {
            try {
                val parentId = call.parameters["parentId"]?.toInt() ?: throw BadRequestException("Invalid parent id")
                val multiProducts = multiProductDao.getMultiProductsForAProductId(parentProductId = parentId)
                call.respond(multiProducts)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "There have some problem")
            }
        }

        put("/updateAMultiProduct") {
            try {
                val multiProduct = call.receive<MultiProduct>()
                multiProductDao.updateAMultiProduct(multiProduct = multiProduct)
                call.respond("updated")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "There have some problem")
            }
        }

        delete("/deleteAMultiProduct/{multiProductId}") {
            try {
                val multiProductId =
                    call.parameters["multiProductId"]?.toInt() ?: throw BadRequestException("Invalid multi product id")
                multiProductDao.deleteAMultiProduct(multiProductId = multiProductId)
                call.respond("deleted")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "There have some problem")
            }
        }
    }
}