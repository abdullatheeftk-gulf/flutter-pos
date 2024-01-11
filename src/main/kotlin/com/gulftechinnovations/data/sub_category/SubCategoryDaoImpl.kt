package com.gulftechinnovations.data.sub_category

import com.gulftechinnovations.database.dbQuery
import com.gulftechinnovations.database.resultRowToSubCategory
import com.gulftechinnovations.database.tables.SubCategoryTable
import com.gulftechinnovations.model.SubCategory
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class SubCategoryDaoImpl:SubCategoryDao {
    override suspend fun insertSubCategory(subCategory: SubCategory):Int {
       return dbQuery {
            SubCategoryTable.insert {
                it[categoryId] = subCategory.categoryId
                it[subCategoryName] = subCategory.subCategoryName
                it[noOfTimesOrdered] = subCategory.noOfTimesOrdered
            }[SubCategoryTable.id].value
        }
    }

    override suspend fun getOneSubCategoryById(subCategoryId: Int): SubCategory? {
       return dbQuery {
            SubCategoryTable.select { SubCategoryTable.id eq subCategoryId }.map {
                SubCategoryTable.resultRowToSubCategory(it)
            }.singleOrNull()
        }
    }

    override suspend fun getOneSubCategoryByName(subCategoryName: String): SubCategory? {
        return dbQuery {
            SubCategoryTable.select { SubCategoryTable.subCategoryName eq subCategoryName }.map {
                SubCategoryTable.resultRowToSubCategory(it)
            }.singleOrNull()
        }
    }

    override suspend fun getAllSubCategoriesByCategoryId(categoryId: Int) :List<SubCategory>{
        return dbQuery {
            SubCategoryTable.select { SubCategoryTable.categoryId eq categoryId }.map {
                SubCategoryTable.resultRowToSubCategory(it)
            }
        }
    }

    override suspend fun getAllSubCategories(): List<SubCategory> {
        return dbQuery {
            SubCategoryTable.selectAll().map {
                SubCategoryTable.resultRowToSubCategory(it)
            }
        }
    }

    override suspend fun updateASubCategory(subCategory: SubCategory) {
        dbQuery {
            SubCategoryTable.update({
                SubCategoryTable.id eq subCategory.subCategoryId
            }) {
                it[id] = subCategory.subCategoryId
                it[categoryId] = subCategory.categoryId
                it[subCategoryName] = subCategory.subCategoryName
                it[noOfTimesOrdered] = subCategory.noOfTimesOrdered
            }
        }
    }

    override suspend fun updateASubCategoryNoOfTimesOrdered(subCategoryId: Int) {
        dbQuery {
            val value = SubCategoryTable.select { SubCategoryTable.id eq subCategoryId }.map {
                SubCategoryTable.resultRowToSubCategory(it)
            }.singleOrNull()?.noOfTimesOrdered ?: 0

            SubCategoryTable.update({SubCategoryTable.id eq  subCategoryId}) {
                it[noOfTimesOrdered] = value+1
            }
        }
    }

    override suspend fun deleteASubCategory(subCategoryId: Int) {
        dbQuery {
            SubCategoryTable.deleteWhere {
               id eq subCategoryId
            }
        }
    }
}