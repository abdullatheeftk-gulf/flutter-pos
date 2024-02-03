package com.gulftechinnovations.model

import kotlinx.serialization.Serializable

@Serializable
data class UserToUpdate(
    val oldUserPassword:String,
    val newUserPassword:String
)
