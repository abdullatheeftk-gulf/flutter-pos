package com.gulftechinnovations.service

import com.gulftechinnovations.database.dbQuery
import com.gulftechinnovations.database.rowToStudent
import com.gulftechinnovations.database.tables.StudentTable
import com.gulftechinnovations.model.Student
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

class StudentServiceImpl:StudentService {
    override suspend fun addStudent(student: Student): Int {
        return  dbQuery {
            StudentTable.insert {
                it[name] = student.name
                it[mark] = student.mark
            }[StudentTable.id].value
        }
    }

    override suspend fun getAllStudents(): List<Student> {
        return  dbQuery {
            StudentTable.selectAll().map {
                StudentTable.rowToStudent(it)
            }
        }
    }
}