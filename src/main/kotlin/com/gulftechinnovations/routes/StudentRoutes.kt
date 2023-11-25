package com.gulftechinnovations.routes

import com.google.cloud.storage.Blob
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import com.gulftechinnovations.model.Student
import com.gulftechinnovations.service.StudentService
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import kotlin.coroutines.coroutineContext

fun Routing.studentRoutes(
    studentService: StudentService,
    config: ApplicationConfig,
) {
    val applicationId = config.property("gcp.projectName").getString()
    val bucketName = config.property("gcp.storageBucketName").getString()



    route("/student") {

        post("/addStudent") {
            try {
                val student = call.receive<Student>()
                val id = studentService.addStudent(student = student)
                call.respond(status = HttpStatusCode.Created, id)
            } catch (e: Exception) {
                call.respond(status = HttpStatusCode.BadRequest, e.message ?: "There have some problem")
            }
        }

        get("/getAllStudents") {
            try {
                val students = studentService.getAllStudents()
                call.respond(HttpStatusCode.OK, students)
            } catch (e: Exception) {
                call.respond(status = HttpStatusCode.BadRequest, e.message ?: "There have some problem")
            }
        }

        post("/uploadAnImage") {
            try {

                var fileName: String
                val multipartDart = call.receiveMultipart()
                var result = ""

                multipartDart.forEachPart { part ->

                    when (part) {

                        is PartData.FileItem -> {
                            fileName = part.originalFileName as String
                            val inputStream = part.streamProvider()
                            result = uploadAnObjectToGoogleCloudStorageAsInputStream(
                                projectId = applicationId,
                                bucketName = bucketName,
                                objectName = fileName,
                                inputStream = inputStream
                            )
                        }

                        else -> {}
                    }
                    part.dispose()
                }
                call.respondText(result)
            } catch (e: Exception) {
                call.respond(status = HttpStatusCode.BadRequest, e.message ?: "There have some problem")
            }
        }



        get("/downloadAnImage/{filename}") {
            try {
                val fileName = call.parameters["filename"] ?: throw Exception("File name is empty")
                val result = downloadObjectAsBytes(
                    projectId = applicationId,
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

        delete("/deleteAnImage/{imageName}") {
            try {
                val objectName = call.parameters["imageName"] ?: throw Exception("File name is empty")
                val result = deleteAnImage(
                    projectId = applicationId,
                    bucketName = bucketName,
                    objectName = objectName,
                )
                call.respond(result)

            } catch (e: Exception) {
                call.respond(status = HttpStatusCode.BadRequest, e.message ?: "There have some problem")
            }
        }

    }
}


@Throws(IOException::class)
suspend fun uploadAnObjectToGoogleCloudStorageAsInputStream(
    projectId: String,
    bucketName: String,
    objectName: String,
    inputStream: InputStream
): String {
    return withContext(coroutineContext) {
        val storage: Storage = StorageOptions.newBuilder().setProjectId(projectId).build().service
        val blobId: BlobId = BlobId.of(bucketName, objectName)

        val blobInfo: BlobInfo = BlobInfo.newBuilder(blobId).build()

        val preCondition: Storage.BlobWriteOption = if (storage.get(bucketName, objectName) == null) {
            Storage.BlobWriteOption.doesNotExist()
        } else {
            Storage.BlobWriteOption.generationMatch(
                storage.get(bucketName, objectName).generation
            )
        }

        val blob = storage.createFrom(blobInfo, inputStream, preCondition)

        blob.blobId.toGsUtilUri()
    }

}

@Throws(IOException::class)
suspend fun uploadAnObjectToGoogleCloudStorageAsByteArray(
    projectId: String,
    bucketName: String,
    objectName: String,
    byteArray: ByteArray
): String {

    return withContext(coroutineContext) {
        val storage: Storage = StorageOptions.newBuilder().setProjectId(projectId).build().service
        val blobId: BlobId = BlobId.of(bucketName, objectName)

        val blobInfo: BlobInfo = BlobInfo.newBuilder(blobId).build()

        val preCondition: Storage.BlobTargetOption = if (storage.get(bucketName, objectName) == null) {
            Storage.BlobTargetOption.doesNotExist()
        } else {
            Storage.BlobTargetOption.generationMatch(
                storage.get(bucketName, objectName).generation
            )
        }

        val blob = storage.create(blobInfo, byteArray,preCondition)


        blob.blobId.toGsUtilUri()
    }

}

suspend fun deleteAnImage(
    projectId: String,
    bucketName: String,
    objectName: String,
): String {
    return withContext(coroutineContext) {
        val storage: Storage = StorageOptions.newBuilder().setProjectId(projectId).build().service
        val blob: Blob = storage.get(bucketName, objectName) ?: throw Exception("There is no image with this name")

        val preCondition: Storage.BlobSourceOption = Storage.BlobSourceOption.generationMatch(blob.generation)

        storage.delete(bucketName, objectName, preCondition)

        "Deleted $objectName"
    }
}

@Throws(IOException::class)
suspend fun downloadObjectAsBytes(
    projectId: String,
    bucketName: String,
    objectName: String,
): ByteArray {
    return withContext(coroutineContext) {

        val storage: Storage = StorageOptions.newBuilder().setProjectId(projectId).build().service
        storage.readAllBytes(bucketName, objectName)

    }
}