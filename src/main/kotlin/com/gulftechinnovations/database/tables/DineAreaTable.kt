package com.gulftechinnovations.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object DineAreaTable:IntIdTable() {
    val name = varchar(name = "name", length = 128).uniqueIndex()
}