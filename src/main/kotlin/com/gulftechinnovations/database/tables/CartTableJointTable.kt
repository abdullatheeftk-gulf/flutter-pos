package com.gulftechinnovations.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object CartTableJointTable:IntIdTable() {
    val tableId = integer(name = "tableId").references(ref = DineInTableTable.id, onDelete = ReferenceOption.SET_NULL).nullable()
    val cartId = integer(name = "cartId").references(ref = CartTable.id, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
    val noOfChairRequired = integer(name = "chairRequired")
    val dineTableName = varchar(name = "dineTableName", length = 128).nullable()
}