@file:Suppress("CAST_NEVER_SUCCEEDS")

package com.gulftechinnovations.plugins

import com.gulftechinnovations.routes.studentRoutes
import com.gulftechinnovations.service.StudentService
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    config: ApplicationConfig,
    studentService: StudentService
) {

    routing {
        get("/") {
            call.respondText("Unipospro")
        }


        studentRoutes(studentService = studentService, config = config)









        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }
}
