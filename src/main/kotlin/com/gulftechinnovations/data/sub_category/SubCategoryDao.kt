package com.gulftechinnovations.data.sub_category

import com.gulftechinnovations.model.Category
import com.gulftechinnovations.model.SubCategory

interface SubCategoryDao {
    suspend fun insertSubCategory(subCategory: SubCategory):Int

    suspend fun getOneSubCategoryById(subCategoryId:Int): SubCategory?

    suspend fun getAllSubCategoriesByCategoryId(categoryId:Int): List<SubCategory>

    suspend fun getOneSubCategoryByName(subCategoryName:String): SubCategory?

    suspend fun getAllSubCategories():List<SubCategory>

    suspend fun updateASubCategory(subCategory: SubCategory)

    suspend fun updateASubCategoryNoOfTimesOrdered(subCategoryId: Int)

    suspend fun deleteASubCategory(subCategoryId: Int)

}