package com.gulftechinnovations.model

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    var categoryId:Int = 0,
    val categoryName:String,
    val noOfTimesOrdered:Int=0,
    val subCategories:MutableList<SubCategory> = mutableListOf()
)
