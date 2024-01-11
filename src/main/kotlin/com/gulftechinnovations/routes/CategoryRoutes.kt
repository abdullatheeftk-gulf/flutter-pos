package com.gulftechinnovations.routes

import com.gulftechinnovations.data.category.CategoryDao
import com.gulftechinnovations.data.sub_category.SubCategoryDao
import com.gulftechinnovations.database.tables.CategoryTable
import com.gulftechinnovations.model.Category
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SortOrder

fun Routing.categoryRoutes(
    categoryDao: CategoryDao,
    subCategoryDao: SubCategoryDao
) {

    route("/category") {

        post("/addCategory") {
            try {
                val category = call.receive<Category>()
                val value = categoryDao.insertCategory(category = category)
                call.respond(status = HttpStatusCode.Created, message = value)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, message = e.message ?: "There have some problem")
            }
        }

        get("/getAllCategories") {
            try {

                val sortOrderByIdParameter = call.request.queryParameters["sortOrderById"]
                val sortOrderByCategoryNameParameter = call.request.queryParameters["sortOrderByCategoryName"]
                val sortOrderByCategoryNoOfTimesOrderedParameter =
                    call.request.queryParameters["sortOrderByCategoryNoOfTomesOrdered"]

                var sortOrderById: Pair<Column<EntityID<Int>>, SortOrder>? = null
                var sortOrderByCategoryName: Pair<Column<String>, SortOrder>? = null
                var sortOrderByNoOfTimesOrdered: Pair<Column<Int>, SortOrder>? = null


                if (sortOrderByIdParameter != null) {
                    sortOrderById = when (sortOrderByIdParameter) {
                        "DESC" -> {
                            Pair(first = CategoryTable.id, second = SortOrder.DESC)
                        }
                        "ASC" -> {
                            Pair(first = CategoryTable.id, second = SortOrder.ASC)
                        }
                        else -> {
                            throw  BadRequestException("Invalid Sort order by id parameter")
                        }
                    }
                }

                if (sortOrderByCategoryNameParameter != null) {
                    sortOrderByCategoryName = when (sortOrderByCategoryNameParameter) {
                        "DESC" -> {
                            Pair(first = CategoryTable.categoryName, second = SortOrder.DESC)
                        }
                        "ASC" -> {
                            Pair(first = CategoryTable.categoryName, second = SortOrder.ASC)
                        }
                        else -> {
                            throw  BadRequestException("Invalid Sort order by id parameter")
                        }
                    }
                }

                if (sortOrderByCategoryNoOfTimesOrderedParameter != null) {
                    sortOrderByNoOfTimesOrdered = when (sortOrderByCategoryNoOfTimesOrderedParameter) {
                        "DESC" -> {
                            Pair(first = CategoryTable.noOfTimesOrdered, second = SortOrder.DESC)
                        }
                        "ASC" -> {
                            Pair(first = CategoryTable.noOfTimesOrdered, second = SortOrder.ASC)
                        }
                        else -> {
                            throw  BadRequestException("Invalid Sort order by id parameter")
                        }
                    }
                }


                val categories = categoryDao.getAllCategory(
                    sortOrderById = sortOrderById,
                    sortOrderByCategoryName = sortOrderByCategoryName,
                    sortOrderByNoOfTimesOrdered = sortOrderByNoOfTimesOrdered

                )
                categories.map {
                    val subCategories = subCategoryDao.getAllSubCategoriesByCategoryId(it.categoryId)
                    it.subCategories.addAll(subCategories)
                }
                call.respond(hashMapOf("result" to categories))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.ExpectationFailed, e.message ?: "There have some problem")
            }
        }

        get("/getACategoryById/{id}") {
            try {
                val id = call.parameters["id"]?.toInt() ?: throw Exception("Invalid category id")
                val category = categoryDao.getOneCategoryById(categoryId = id)
                    ?: throw BadRequestException("No categories with this id")
                call.respond(category)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "There have some problem")
            }
        }
        put("/updateACategory") {
            try {
                val category = call.receive<Category>()
                categoryDao.updateACategory(category = category)
                call.respondText("Updated")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "There have some problem")
            }
        }
        delete("/deleteACategory/{id}") {
            try {
                val id = call.parameters["id"]?.toInt() ?: throw Exception("Invalid category id")
                categoryDao.deleteACategory(categoryId = id)
                call.respondText("deleted")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "There have some problem")
            }
        }

    }

}