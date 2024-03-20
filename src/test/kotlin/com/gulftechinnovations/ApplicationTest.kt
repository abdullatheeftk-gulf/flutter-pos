package com.gulftechinnovations

import com.gulftechinnovations.data.admin.AdminUserDao
import com.gulftechinnovations.data.admin.AdminUserDaoImpl
import com.gulftechinnovations.data.cart.CartDaoImpl
import com.gulftechinnovations.data.category.CategoryDao
import com.gulftechinnovations.data.category.CategoryDaoImpl
import com.gulftechinnovations.data.multi_product.MultiProductDaoImpl
import com.gulftechinnovations.data.product.ProductDaoImpl
import com.gulftechinnovations.data.sub_category.SubCategoryDaoImpl
import com.gulftechinnovations.data.user.UserDao
import com.gulftechinnovations.data.user.UserDaoImpl
import com.gulftechinnovations.plugins.*
import com.gulftechinnovations.service.StudentService
import com.gulftechinnovations.service.StudentServiceImpl
import io.ktor.client.*
/*import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.**/
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlin.test.*

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {

            /*val client = HttpClient(OkHttp) {
                install(ContentNegotiation) {
                    json()
                }
            }*/
            val studentService: StudentService by lazy {
                StudentServiceImpl()
            }

            val userDao: UserDao by lazy {
                UserDaoImpl()
            }
            val adminUserDao: AdminUserDao by lazy {
                AdminUserDaoImpl()
            }

            val categoryDao: CategoryDao by lazy {
                CategoryDaoImpl()
            }

            val subCategoryDao by lazy {
                SubCategoryDaoImpl()
            }

            val productDao by lazy {
                ProductDaoImpl()
            }

            val multiProductDao by lazy {
                MultiProductDaoImpl()
            }

            val cartDao by lazy {
                CartDaoImpl()
            }

            /*configureRouting(
                config = environment.config,
                studentService = studentService,
                userDao = userDao,
                adminUserDao = adminUserDao,
                categoryDao = categoryDao,
                subCategoryDao = subCategoryDao,
                productDao = productDao,
                multiProductDao = multiProductDao,
                cartDao = cartDao,
                client = client,
                translate = Tre

            )*/
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }
}
