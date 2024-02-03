package com.gulftechinnovations.model

import kotlinx.serialization.Serializable

@Serializable
data class AdminResponse(
    val adminUser: AdminUser,
    val token: String
)
