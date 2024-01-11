package com.gulftechinnovations.data.category

import com.gulftechinnovations.database.tables.CategoryTable
import com.gulftechinnovations.model.Category
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SortOrder

interface CategoryDao {

    suspend fun insertCategory(category: Category):Int

    suspend fun getOneCategoryById(categoryId:Int):Category?

    suspend fun getOneCategoryByName(categoryName:String):Category?

    suspend fun getAllCategory(
        sortOrderById:Pair<Column<EntityID<Int>>,SortOrder>? = Pair(first = CategoryTable.id, second = SortOrder.ASC),
        sortOrderByCategoryName:Pair<Column<String>,SortOrder>? = null,
        sortOrderByNoOfTimesOrdered:Pair<Column<Int>,SortOrder>?=null
    ):List<Category>

    suspend fun updateACategory(category: Category):Int

    suspend fun updateACategoryNoOfTimesOrdered(categoryId: Int)

    suspend fun deleteACategory(categoryId: Int)

}