package com.gulftechinnovations.model

import kotlinx.serialization.Serializable

@Serializable
data class AdminPasswordToReset(
    val oldAdminUser: AdminUser,
    val newAdminUser: AdminUser
)