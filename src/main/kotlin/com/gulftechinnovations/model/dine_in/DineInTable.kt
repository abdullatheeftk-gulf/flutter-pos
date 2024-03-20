package com.gulftechinnovations.model.dine_in

import kotlinx.serialization.Serializable

@Serializable
data class DineInTable(
    val id: Int,
    val name: String,
    val noOfSeats: Int,
    val noOfSeatsOccupied: Int,
    val areaId: Int,
)
