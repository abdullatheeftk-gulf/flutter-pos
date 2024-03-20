package com.gulftechinnovations.database.tables.test_samples

import org.jetbrains.exposed.dao.id.IntIdTable

object AbTable:IntIdTable() {
    val name = varchar(name = "name", length = 255)
}