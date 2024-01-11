package com.gulftechinnovations.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object CartProductTable: IntIdTable() {
    val cartId = integer(name = "cart_id_ref").references(ref = CartTable.id,onDelete = ReferenceOption.CASCADE)
    val productId = integer(name="productId")
    val productCartName = varchar(name = "productCartName", length = 512)
    val productLocalCartName = varchar(name = "productLocalCartName", length = 512).nullable()
    val noOfItemsOrdered = float(name = "nonOfItemsOrdered")
    val note = varchar(name = "info", length = 512).nullable()
}