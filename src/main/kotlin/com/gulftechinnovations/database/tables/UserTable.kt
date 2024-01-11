package com.gulftechinnovations.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object UserTable: IntIdTable(){
    val userName = varchar(name = "userName", length = 128).uniqueIndex()
    val userPassword = varchar(name = "userPassword", length = 32)
}