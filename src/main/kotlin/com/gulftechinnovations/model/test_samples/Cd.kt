package com.gulftechinnovations.model.test_samples

import kotlinx.serialization.Serializable

@Serializable
data class Cd(
    val id:Int,
    val name:String,
    val price:Float,
    val abId:Int?
)
