package com.gulftechinnovations.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object DineInTableTable:IntIdTable() {
    val name = varchar(name = "name", length = 128).uniqueIndex()
    val noOfSeats = integer(name="noOfSeats")
    val noOfSeatsOccupied = integer(name="noOfSeatsOfOccupied")
    val areaId = integer(name="areaId").references(DineAreaTable.id, onDelete = ReferenceOption.CASCADE)
}