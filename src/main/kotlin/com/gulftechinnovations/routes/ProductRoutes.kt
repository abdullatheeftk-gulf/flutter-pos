package com.gulftechinnovations.routes

import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import com.gulftechinnovations.data.product.ProductDao
import com.gulftechinnovations.model.Product
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import kotlin.coroutines.coroutineContext

fun Routing.productRoutes(
    productDao: ProductDao,
    config: ApplicationConfig,
) {

    val projectId = config.property("gcp.projectName").getString()
    val bucketName = config.property("gcp.storageBucketName").getString()


    route("/product") {

        post("/addAProduct") {
            try {
                val product = call.receive<Product>()
                println(product)
                if (product.categories.isEmpty()) {
                    throw BadRequestException("Product is not added to any category")
                }
                val value = productDao.insertProduct(product = product)
                call.respond(value)
            } catch (e: ExposedSQLException) {

                call.respond(HttpStatusCode.Conflict, e.message ?: "There have some problem")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "There have some problem")
            }

        }

        /*post("/samplePhotoAdd/{productId}") {
            try {
                val multipartData = call.receiveMultipart()
                val productId = call.parameters["productId"]?.toInt() ?: throw BadRequestException("Invalid product Id")

                multipartData.forEachPart { partData ->
                    when (partData) {

                        is PartData.FileItem -> {
                            val fileName = partData.originalFileName as String
                            val part = fileName.split(".")[1]
                            println("------------------")
                            println(part)
                            val fileBytes = partData.streamProvider().readAllBytes()
                            //val bi = ImageIO.read(fileBytes)

                            val productName = productDao.getAProductNameById(productId = productId)
                            File("images/$productName.$part").writeBytes(fileBytes)
                            launch(Dispatchers.IO) {
                                productDao.updateAProductPhoto(
                                    productId = productId,
                                    productName = "images/$productName.$part"
                                )
                            }
                        }

                        else -> Unit
                    }
                }

                call.respond("Uploaded Photo")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "There have some problem")
            }
        }*/

        post("/addAProductPhoto/{productId}") {

            try {

                val multipartData = call.receiveMultipart()
                val productId = call.parameters["productId"]?.toInt() ?: throw BadRequestException("Invalid product Id")

                var imageByteArray: ByteArray

                multipartData.forEachPart { part ->
                    when (part) {
                        is PartData.FileItem -> {
                            val originalFileName = part.originalFileName as String
                            val listOfString = originalFileName.split(".")
                            val mime = listOfString.last()

                            val inputStream = part.streamProvider()
                            val bufferedImage = ImageIO.read(inputStream)
                            val width = bufferedImage.width
                            val height = bufferedImage.height


                            val productName = productDao.getAProductNameById(productId = productId)

                            imageByteArray =
                                convertAndResizeBufferImageToFile(
                                    bi = bufferedImage,
                                    mime = mime,
                                    width = width,
                                    height = height,
                                )


                            // enable only when release mode
                            /*uploadAnImageToGoogleCloudStorageAsByteArray(
                                projectId = projectId,
                                bucketName = bucketName,
                                objectName = "$productName.$mime",
                                byteArray = imageByteArray,
                                mime = mime
                            )*/

                            // for local
                            println(imageByteArray.size)
                            File("images/$productName.$mime").writeBytes(imageByteArray)

                            launch(Dispatchers.IO) {
                                productDao.updateAProductPhoto(
                                    productId = productId,
                                    productName = "$productName.$mime"
                                )
                            }


                        }

                        else -> Unit
                    }
                }
                call.respond("Success")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "There have some problem")
            }
        }

        put("/removeAndUpdateProductPhoto/{productId}"){
            try {
                val multipartData = call.receiveMultipart()
                val productId = call.parameters["productId"]?.toInt() ?: throw BadRequestException("Invalid product Id")

                val product = productDao.getProductsById(productId = productId) ?: throw BadRequestException("No Products with this id")
                val productImage = product.productImage
                productImage?.let {pImage->
                    try{
                        File("images/$pImage").delete()
                    }catch (e:IOException){
                        println(e.message)
                    }
                }

                var imageByteArray: ByteArray

                multipartData.forEachPart { part ->
                    when (part) {
                        is PartData.FileItem -> {
                            val originalFileName = part.originalFileName as String
                            val listOfString = originalFileName.split(".")
                            val mime = listOfString.last()

                            val inputStream = part.streamProvider()
                            val bufferedImage = ImageIO.read(inputStream)
                            val width = bufferedImage.width
                            val height = bufferedImage.height


                            val productName = productDao.getAProductNameById(productId = productId)

                            imageByteArray =
                                convertAndResizeBufferImageToFile(
                                    bi = bufferedImage,
                                    mime = mime,
                                    width = width,
                                    height = height,
                                )


                            // enable only when release mode
                            /*uploadAnImageToGoogleCloudStorageAsByteArray(
                                projectId = projectId,
                                bucketName = bucketName,
                                objectName = "$productName.$mime",
                                byteArray = imageByteArray,
                                mime = mime
                            )*/

                            // for local
                            File("images/$productName.$mime").writeBytes(imageByteArray)

                            launch(Dispatchers.IO) {
                                productDao.updateAProductPhoto(
                                    productId = productId,
                                    productName = "$productName.$mime"
                                )
                            }


                        }

                        else -> Unit
                    }
                }
                call.respond("Image updated")


            }catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "There have some problem")
            }

        }

        get("/getAllProduct") {
            try {
                val products = productDao.getAllProducts()

                call.respond(products)
                //call.respond(HttpStatusCode.Conflict)

            } catch (e: Exception) {
                call.respond(HttpStatusCode.ExpectationFailed, e.message ?: "There have some problem")
            }
        }

        get("/searchAProduct/{search}") {
            try {
                val searchText = call.parameters["search"] ?: throw Exception("Invalid search keyword")
                val products = productDao.searchProductsByName("%$searchText%")

                call.respond(products)

            } catch (e: Exception) {
                call.respond(HttpStatusCode.ExpectationFailed, e.message ?: "There have some problem")
            }

        }

        get("/getProductsByCategory/{categoryId}") {
            try {
                HttpStatusCode.NoContent
                val categoryId =
                    call.parameters["categoryId"]?.toInt() ?: throw BadRequestException("Invalid categoryId")
                val products = productDao.getProductsByCategoryId(categoryId = categoryId)
                if (products.isEmpty()) {
                    call.respond(products)
                } else {
                    call.respond(products)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "There have some problem")
            }
        }

        put("/updateAProduct") {
            try {
                val product = call.receive<Product>()
                productDao.updateAProduct(product = product)
                call.respond("updated")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "There have some problem")

            }
        }

        delete("/deleteAProduct/{id}") {
            try {
                val productId = call.parameters["id"]?.toInt() ?: throw BadRequestException("There have some problem")
                val product = productDao.getProductsById(productId)?:throw  Exception("No products with this productId $productId")
                val productImage = product.productImage

                productDao.deleteAProduct(productId = productId)

                productImage?.let {
                    File("images/$it").delete()
                }

                call.respond("deleted")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "There have some problem")
            }
        }

        get ("/getAProductById/{id}"){
            try {
                val productId = call.parameters["id"]?.toInt() ?: throw Exception("There have some problem")
                val product = productDao.getProductsById(productId = productId) ?: throw  Exception("No products with this id")
                call.respond(product)
            }catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "There have some problem")
            }
        }

        post("/updateProductPhoto/{productId}") {
            try {
                val productId = call.parameters["productId"]?.toInt()?:throw  Exception("Invalid productId")
                val imageBytes = call.receive<ByteArray>()
                val inputArrayStream = ByteArrayInputStream(imageBytes)

                val bufferedImage = ImageIO.read(inputArrayStream)
                val width = bufferedImage.width
                val height = bufferedImage.height


                val fileName = call.request.queryParameters["name"] ?: throw Exception("no name")

                val names = fileName.split(".")
                val mime = names[1]

                val bytes = convertAndResizeBufferImageToFile(
                    bi = bufferedImage,
                    mime = mime,
                    width = width,
                    height = height
                )

                val previousProduct = productDao.getProductsById(productId) ?: throw Exception("No product with this id")


                previousProduct.productImage?.let {productImage->
                    File("images/$productImage").delete()
                }


                val productName = productDao.getAProductNameById(productId = productId)


                File("images/$productName.$mime").writeBytes(bytes)

                productDao.updateAProductPhoto(productId, "$productName.$mime")

                call.respond("Success")

            } catch (e: Exception) {
                println(e.message)
                call.respond(status = HttpStatusCode.BadRequest, e.message ?: "There have some problem")
            }

        }

        delete("/removeAProductPhoto/{productId}") {
            try{
                val productId = call.parameters["productId"]?.toInt()?:throw BadRequestException("No product id")
                val product = productDao.getProductsById(productId) ?: throw  BadRequestException("No product with productId:- $productId")
                val productImage = product.productImage

                productImage?.let {image->
                    File("images/$image").delete()
                }

                productDao.updateAProductPhoto(productId, productName = null)

                call.respond("deleted")

            }catch (e: Exception) {
                println(e.message)
                call.respond(status = HttpStatusCode.BadRequest, e.message ?: "There have some problem")
            }
        }

        get("/getSelectedCategories/{productId}") {
            try{
                val productId = call.parameters["productId"]?.toInt() ?:throw  Exception("No product id")
                val categories = productDao.getAProductCategories(productId)
                call.respond(categories)
            }catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "There have some problem")
            }

        }


    }

}

fun convertAndResizeBufferImageToFile(
    bi: BufferedImage,
    mime: String,
    width: Int,
    height: Int,
    ): ByteArray {

    val ratio = if(width>500) width / 500f else 1f
    val targetHeight = (height / ratio).toInt()
    val targetWidth = if(width>500) 500 else width

    val resultImage = bi.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT)
    val outputImage = BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
    outputImage.graphics.drawImage(resultImage, 0, 0, null)

    val baos: ByteArrayOutputStream = ByteArrayOutputStream()
    ImageIO.write(outputImage, mime, baos)
    return baos.toByteArray()
}

@Throws(IOException::class)
suspend fun uploadAnImageToGoogleCloudStorageAsByteArray(
    projectId: String,
    bucketName: String,
    objectName: String,
    byteArray: ByteArray,
    mime: String
): String {

    return withContext(coroutineContext) {
        val storage: Storage = StorageOptions.newBuilder().setProjectId(projectId).build().service
        val blobId: BlobId = BlobId.of(bucketName, objectName)

        val blobInfo: BlobInfo = BlobInfo.newBuilder(blobId)
            .setContentType("image/$mime")
            .build()

        val preCondition: Storage.BlobTargetOption = if (storage.get(bucketName, objectName) == null) {
            Storage.BlobTargetOption.doesNotExist()
        } else {
            Storage.BlobTargetOption.generationMatch(
                storage.get(bucketName, objectName).generation
            )
        }

        val blob = storage.create(blobInfo, byteArray, preCondition)


        blob.mediaLink
    }

}
