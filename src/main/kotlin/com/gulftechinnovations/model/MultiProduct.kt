package com.gulftechinnovations.model

import kotlinx.serialization.Serializable

@Serializable
data class MultiProduct(
    var multiProductId:Int = 0,
    val parentProductId:Int,
    val multiProductName:String,
    val multiProductLocalName:String? = null,
    val multiProductImage:String?=null,
    val info:String? = null
)
