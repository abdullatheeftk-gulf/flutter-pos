package com.gulftechinnovations

import com.google.cloud.translate.TranslateOptions
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
import com.gulftechinnovations.plugins.configureHTTP
import com.gulftechinnovations.plugins.configureRouting
import com.gulftechinnovations.plugins.configureSecurity
import com.gulftechinnovations.plugins.configureSerialization
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




    DatabaseFactory.init(config = environment.config)

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

    val translate =  TranslateOptions.getDefaultInstance().service


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

        translate = translate
    )

    registerAdminUserAddDefaultCategoryValues(adminUserDao = adminUserDao)
}

fun registerAdminUserAddDefaultCategoryValues(adminUserDao: AdminUserDao) {
    runBlocking {
        val adminUser = adminUserDao.getOneAdminUser(adminPassword = "741")
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




