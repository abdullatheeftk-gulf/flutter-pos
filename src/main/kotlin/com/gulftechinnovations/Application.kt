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
import com.gulftechinnovations.database.DatabaseFactory
import com.gulftechinnovations.database.dbQuery
import com.gulftechinnovations.model.AdminUser
import com.gulftechinnovations.plugins.*
import com.gulftechinnovations.service.StudentService
import com.gulftechinnovations.service.StudentServiceImpl
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    DatabaseFactory.init()

    val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json()
        }
    }

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

    configureSerialization()
    configureHTTP()
    configureSecurity()

    configureRouting(
        studentService = studentService,
        config = environment.config,
        userDao = userDao,
        adminUserDao = adminUserDao,
        cartDao = cartDao,
        categoryDao = categoryDao,
        subCategoryDao = subCategoryDao,
        productDao = productDao,
        multiProductDao = multiProductDao,
        client = client,
    )

    registerAdminUserAddDefaultCategoryValues(adminUserDao = adminUserDao)
}

fun registerAdminUserAddDefaultCategoryValues(adminUserDao: AdminUserDao) {
    runBlocking {
        val adminUser = adminUserDao.getOneAdminUser(adminId = 1)
        if (adminUser == null) {
            dbQuery {
                adminUserDao.insertAdminUser(
                    AdminUser(
                        adminName = "admin",
                        adminPassword = "741",
                        licenseKey = "key"
                    )
                )
            }

        }
    }
}

