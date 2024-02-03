package com.gulftechinnovations.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable


object UserTable: IntIdTable(){
    val userPassword = varchar(name = "userPassword", length = 32)
        .uniqueIndex()

}