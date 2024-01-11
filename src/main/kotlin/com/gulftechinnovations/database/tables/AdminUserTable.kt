package com.gulftechinnovations.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object AdminUserTable: IntIdTable() {
    val adminUserName = varchar(name="adminUser", length = 128).default(defaultValue = "admin")
    val adminPassword = varchar(name="adminPassword", length = 32).default(defaultValue = "741")
    val licenseKey = varchar(name = "licenseKey", length = 64)
}