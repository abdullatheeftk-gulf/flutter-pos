package com.gulftechinnovations.model

data class CartProduct(
    val cartId:Int,
    val productId:Int,
    val productCartName:String,
    val productLocalCartName:String? = null,
    val noOfItemsOrdered:Float,
    val note:String?
)
