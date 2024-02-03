package com.gulftechinnovations.plugins

import com.detectlanguage.DetectLanguage
import com.google.cloud.translate.Translate
import com.gulftechinnovations.data.admin.AdminUserDao
import com.gulftechinnovations.data.cart.CartDao
import com.gulftechinnovations.data.category.CategoryDao
import com.gulftechinnovations.data.multi_product.MultiProductDao
import com.gulftechinnovations.data.product.ProductDao
import com.gulftechinnovations.data.sub_category.SubCategoryDao
import com.gulftechinnovations.data.user.UserDao
import com.gulftechinnovations.model.Transliterate
import com.gulftechinnovations.routes.*
import com.gulftechinnovations.service.StudentService
import com.gulftechinnovations.translation.deepLTranslate
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.suuft.libretranslate.Language
import net.suuft.libretranslate.Translator
import org.apache.logging.log4j.kotlin.KotlinLogger
import org.apache.logging.log4j.kotlin.Logging
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO

fun Application.configureRouting(
    config: ApplicationConfig,
    studentService: StudentService,
    userDao: UserDao,
    adminUserDao: AdminUserDao,
    categoryDao: CategoryDao,
    subCategoryDao: SubCategoryDao,
    productDao: ProductDao,
    multiProductDao: MultiProductDao,
    cartDao: CartDao,
    client: HttpClient,

    translate: Translate,
) {


    val projectId = config.property("gcp.projectName").getString()
    val bucketName = config.property("gcp.storageBucketName").getString()

    routing {
        get("/") {
            call.respondText("Unipos Android pos")
        }

        get("/translate/{text}") {
            try {
                val text = call.parameters["text"] ?: throw Exception("null text")
                val translatedText = Translator.translate(Language.ENGLISH, Language.ARABIC, text)
                DetectLanguage.apiKey = "1c3d0cc71989aa146ffa27284b92c0b9"
                val result = DetectLanguage.detect(translatedText)[0]
                println(result.language)
                print("-------------$translatedText")
                call.respondText(translatedText)

            } catch (e: Exception) {
                println(e.toString())
                call.respond(HttpStatusCode.BadRequest, message = e.message ?: "There have some problem")

            }
        }

        get("transliterate/{text}") {
            try {
                val text = call.parameters["text"] ?: throw Exception("null text")
                val response = client.get(urlString = "https://transliterate.qcri.org/en2ar/$text")
                if (response.status.value == 200) {
                    val value = response.body<Transliterate>()
                    call.respondText(value.results)
                } else {
                    throw Exception("Expectation failed")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, message = e.message ?: "There have some problem")
            }
        }

        get("/getAllAdminUsers") {
            val users = adminUserDao.getAllAdminUsers()
            call.respond(users)
        }

        get("/downloadAnImage/{filename}") {
            try {
                val fileName = call.parameters["filename"] ?: throw Exception("File name is empty")
                val result = downloadObjectAsBytes(
                    projectId = projectId,
                    bucketName = bucketName,
                    objectName = fileName
                )
                call.respondBytes(
                    bytes = result,
                    contentType = ContentType.Image.Any,
                    status = HttpStatusCode.OK
                )

            } catch (e: Exception) {
                call.respond(status = HttpStatusCode.BadRequest, e.message ?: "There have some problem")
            }
        }

        post("/deepLTranslate") {
            try {
                val text = call.receiveText()
                val translatedText = deepLTranslate(text)
                call.respondText(translatedText)
            } catch (e: Exception) {
                call.respond(status = HttpStatusCode.BadRequest, e.message ?: "There have some problem")
            }

        }

        post("/googleTranslate") {
            try {
                val text = call.receiveText()
                println(text)
                val result = translate.translate(
                    text,
                    Translate.TranslateOption.sourceLanguage("en"),
                    Translate.TranslateOption.targetLanguage("ar")
                )
                call.respond(result.translatedText)
            } catch (e: Exception) {
                println(e.message)
                call.respond(status = HttpStatusCode.BadRequest, e.message ?: "There have some problem")
            }
        }






        studentRoutes(studentService = studentService, config = config)

        adminRoutes(application = application, adminUserDao = adminUserDao)

        userRoutes(userDao = userDao, application = application)

        categoryRoutes(categoryDao = categoryDao, subCategoryDao = subCategoryDao)

        subCategoryRoutes(subCategoryDao = subCategoryDao)

        productRoutes(productDao = productDao, config = config)

        multiProductRoutes(multiProductDao = multiProductDao)

        cartRoutes(cartDao = cartDao, productDao = productDao)


        // Static plugin. Try to access `/static/index.html`
        /*static("/static") {
            resources("static")
        }*/
        staticFiles("/images", File("images/"))
    }
}



