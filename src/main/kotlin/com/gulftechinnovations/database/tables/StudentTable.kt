package com.gulftechinnovations.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object  StudentTable:IntIdTable(){
    val name = varchar(name = "name", length = 32)
    val mark = float(name = "mark")
}
