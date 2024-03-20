package com.gulftechinnovations.data.dine_in.table

import com.gulftechinnovations.model.dine_in.DineInTable

interface DineInTableDao {
    suspend fun insetTable(dineInTable: DineInTable)
    suspend fun getAllTables():List<DineInTable>
    suspend fun getTablesByAreaId(areaId:Int):List<DineInTable>
    suspend fun getTablesByNoOfSeats(noOfSeats:Int):List<DineInTable>
    suspend fun getTablesByNoOfSeatsOccupied(noOfSeatsOccupied:Int):List<DineInTable>
    suspend fun getATableById(id:Int):DineInTable?
    suspend fun getATableByName(name:String):DineInTable?
    suspend fun updateATable(dineInTable: DineInTable)
    suspend fun deleteATable(id:Int)
    suspend fun deleteAllTablesUnderAnArea(areaId: Int)
}