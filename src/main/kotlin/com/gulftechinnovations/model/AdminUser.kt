package com.gulftechinnovations.model

import kotlinx.serialization.Serializable

@Serializable
data class AdminUser(
    var adminId: Int =0,
    val adminName:String,
    val adminPassword:String,
    val licenseKey:String,
    val isLicenceKeyVerified:Boolean = true
)
