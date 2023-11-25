package com.gulftechinnovations.service

import com.gulftechinnovations.model.Student

interface StudentService {

    suspend fun addStudent(student:Student):Int

    suspend fun getAllStudents():List<Student>
}