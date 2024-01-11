package com.gulftechinnovations.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object MultiProductTable: IntIdTable() {
    val parentProductId = integer("product_id_ref").references(ref = ProductTable.id, onDelete = ReferenceOption.CASCADE)
    val multiProductName = varchar(name = "multiProductName", length = 512).uniqueIndex()
    val multiProductLocalName = varchar(name = "multiProductLocalName", length = 512).nullable()
    val multiProductImage = varchar(name = "multiProductImage", length = 128).nullable()
    val info = varchar(name = "info", length = 1024).nullable()
}