package com.gulftechinnovations.model

import kotlinx.serialization.Serializable

@Serializable
data class CartProductItem(
    val noOfItemsOrdered:Float,
    val note:String? = null,
    val cartProductName:String,
    val cartProductLocalName:String?,
    val product:Product?
)
