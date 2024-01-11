package com.gulftechinnovations.routes

import com.gulftechinnovations.data.cart.CartDao
import com.gulftechinnovations.data.product.ProductDao
import com.gulftechinnovations.model.Cart
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun Routing.cartRoutes(cartDao: CartDao, productDao: ProductDao) {

    route("/cart") {

        post("/generateInvoice") {
            println("=====================================")
            try {
                val cart = call.receive<Cart>()
                println(cart)
                val total = cart.total
                var totalFromProducts = 0f
                cart.cartProductItems.forEach {
                    if (it.product == null) {
                        call.respond(HttpStatusCode.BadRequest, message = "null product")
                        return@post
                    }
                    println(it.product.productPrice)
                    println(it.product.productName)
                    totalFromProducts += (it.product.productPrice * it.noOfItemsOrdered)
                }
                println(total)
                println(totalFromProducts)
                if (total != totalFromProducts.toDouble()) {
                    println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                    call.respond(HttpStatusCode.BadRequest, message = "Bad request")
                    return@post
                }

                launch(Dispatchers.IO) {
                    cart.cartProductItems.forEach {
                        productDao.updateAProductNoOfItemsOrdered(it.product?.productId!!, it.noOfItemsOrdered)
                    }
                }

                val inVoiceNo = cartDao.insertCart(cart = cart)
                call.respondText( inVoiceNo.toString())

            } catch (e: Exception) {
                println(e.message)
                call.respond(HttpStatusCode.BadRequest, message = e.message ?: "There have some problem")
            }

        }

        get("/getAllInvoices") {
            try {
                val invoices = cartDao.getAllCarts()
                call.respond(hashMapOf("invoices" to invoices))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, message = e.message ?: "There have some problem")
            }
        }

        get("/getOneInvoiceByInvoiceNo/{invoiceNo}") {
            try {
                val invoiceNo = call.parameters["invoiceNo"]?.toInt() ?: throw Exception("Bad Invoice no")
                val cart = cartDao.getOneCart(invoiceNo = invoiceNo) ?: throw Exception("No Invoice is available")

                call.respond(cart)

            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, message = e.message ?: "There have some problem")
            }
        }

        get("/getOneInvoicesByUserId/{userId}") {
            try {
                val userId = call.parameters["userId"]?.toInt() ?: throw Exception("Bad User id")
                val carts = cartDao.getCartsByUserId(userId = userId)
                call.respond(hashMapOf("carts" to carts))

            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, message = e.message ?: "There have some problem")
            }
        }

        get("/getOneInvoicesByAdminUserId/{adminUserId}") {
            try {
                val adminUserId = call.parameters["adminUserId"]?.toInt() ?: throw Exception("Bad Admin User id")
                val carts = cartDao.getCartsByAdminUserId(adminUserId = adminUserId)
                call.respond(hashMapOf("carts" to carts))

            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, message = e.message ?: "There have some problem")
            }
        }

        put("/updateOneInvoice") {
            try {
                val cart = call.receive<Cart>()
                cartDao.updateOneCart(cart)
                call.respond("Updated")
            }catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, message = e.message ?: "There have some problem")
            }

        }

        delete("/deleteOneInvoice/{cartId}") {
            try {
                val cartId = call.parameters["cartId"]?.toInt() ?: throw  Exception(" invalid cart id")
                cartDao.deleteOneCart(cartId)
                call.respond("Deleted")
            }catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, message = e.message ?: "There have some problem")
            }
        }
    }

}