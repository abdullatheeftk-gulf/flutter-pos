package com.gulftechinnovations.database.tables

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object CategoryProductTable: Table() {
    val categoryId = integer("category_id_ref").references(ref = CategoryTable.id,onDelete = ReferenceOption.CASCADE)
    val productId = integer("product_id_ref").references(ref = ProductTable.id,onDelete = ReferenceOption.CASCADE)
}