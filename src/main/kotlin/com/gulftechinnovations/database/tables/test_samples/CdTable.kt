package com.gulftechinnovations.database.tables.test_samples


import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object CdTable:IntIdTable() {
    val name = varchar(name = "name", length = 255)
    val price = float(name = "price")
    val abId = integer(name = "abId").references(ref = AbTable.id, onDelete = ReferenceOption.SET_NULL).nullable()
}