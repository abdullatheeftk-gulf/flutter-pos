package com.gulftechinnovations.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object ProductTable: IntIdTable() {
    val productName = varchar(name = "productName", length = 512).uniqueIndex()
    val productLocalName = varchar(name = "productLocalName", length = 512/*,collate = "Arabic_CI_AI_KS_WS"*/).nullable()
    val productPrice = float(name = "productPrice")
    val productTaxInPercentage = float(name="productTaxInPercentage")
    val productImage = varchar(name = "productImage", length = 128).nullable()
    val noOfTimesOrdered = float(name = "noOfTimeOrdered").default(defaultValue = 0f)
    val info = varchar(name = "info", length = 128).nullable()
    val subCategoryId = integer(name = "subCategory_id_ref").references(ref = SubCategoryTable.id,onDelete = ReferenceOption.SET_NULL).nullable()
}