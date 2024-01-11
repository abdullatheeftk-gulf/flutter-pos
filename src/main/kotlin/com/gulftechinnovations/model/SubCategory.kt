package com.gulftechinnovations.model

import kotlinx.serialization.Serializable

@Serializable
data class SubCategory(
    var subCategoryId:Int = 0,
    val categoryId:Int,
    val subCategoryName:String,
    val noOfTimesOrdered:Int=0
)
