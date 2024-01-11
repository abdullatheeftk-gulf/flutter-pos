package com.gulftechinnovations.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object SubCategoryTable: IntIdTable() {
    val categoryId = integer(name = "category_id_ref").references(ref = CategoryTable.id, onDelete = ReferenceOption.CASCADE)
    val subCategoryName = varchar(name = "subCategoryName", length = 128)
    val noOfTimesOrdered = integer(name = "noOfTimesOrdered").default(defaultValue = 0)
}