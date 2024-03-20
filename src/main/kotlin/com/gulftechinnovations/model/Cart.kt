package com.gulftechinnovations.model

import com.gulftechinnovations.model.dine_in.DineInTable
import kotlinx.serialization.Serializable

@Serializable
data class Cart(
    val invoiceNo:Int,
    val total:Double,
    val totalTaxAmount:Float = 0f,
    val net:Double,
    val customerName:String? = null,
    val info:String? = null,
    val cartProductItems:List<CartProductItem> = emptyList(),
    val userId:Int?=null,
    val adminUserId :Int? = null,
    val tables:List<DineInTable>? = null,
    val noOfSeatsRequired:Int? = null
)


