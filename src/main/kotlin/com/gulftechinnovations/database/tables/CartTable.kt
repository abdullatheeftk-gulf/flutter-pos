package com.gulftechinnovations.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object CartTable: IntIdTable() {
    val total = double(name = "total")
    val totalTaxAmount = float(name = "totalTaxAmount").default(defaultValue = 0f)
    val  net = double(name = "net")
    val customerName = varchar(name = "customerName",length = 512).nullable().default(defaultValue = null)
    val info = varchar(name = "info", length = 512).nullable()
    val userId = integer(name = "userId").nullable()
    val adminUserId = integer(name = "adminUserId").nullable()
}