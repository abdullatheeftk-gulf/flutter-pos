package com.gulftechinnovations

import com.gulftechinnovations.database.DatabaseFactory
import com.gulftechinnovations.plugins.*
import com.gulftechinnovations.service.StudentService
import com.gulftechinnovations.service.StudentServiceImpl
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    DatabaseFactory.init()

    val studentService:StudentService by lazy {
        StudentServiceImpl()
    }

    configureSerialization()
    configureHTTP()
    configureSecurity()
    configureRouting(studentService = studentService, config = environment.config)
}
