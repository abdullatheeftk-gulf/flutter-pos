package com.gulftechinnovations.database

import com.gulftechinnovations.database.tables.StudentTable
import com.gulftechinnovations.model.Student
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }

fun StudentTable.rowToStudent(row: ResultRow) = Student(
    id = row[id].value,
    name = row[name],
    mark = row[mark]
)



