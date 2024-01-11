package com.gulftechinnovations.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object CategoryTable: IntIdTable() {
    val categoryName = varchar("categoryName", length = 128).uniqueIndex()
    val noOfTimesOrdered = integer(name = "noOfTimesOrdered").default(defaultValue = 0)
}