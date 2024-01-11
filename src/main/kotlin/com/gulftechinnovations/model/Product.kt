package com.gulftechinnovations.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    var productId:Int=0,
    val productName:String,
    val productLocalName:String?=null,
    val productPrice:Float,
    val productTaxInPercentage:Float,
    val productImage:String?=null,
    val noOfTimesOrdered:Float = 0f,
    val info:String?= null,
    val subCategoryId:Int? = null,
    val multiProducts:List<MultiProduct> = emptyList(),
    val categories:List<Int>
)
