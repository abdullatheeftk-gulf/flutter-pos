package com.gulftechinnovations.model

import kotlinx.serialization.Serializable

@Serializable
data class Cart(
    val invoiceNo:Int,
    val total:Double,
    val totalTaxAmount:Float = 0f,
    val net:Double,
    val customerName:String? = null,
    val info:String? = null,
    var cartProductItems:List<CartProductItem> = emptyList(),
    val userId:Int?=null,
    val adminUserId :Int? = null,
)


