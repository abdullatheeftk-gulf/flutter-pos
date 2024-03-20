package com.gulftechinnovations.data.dine_in.area

import com.gulftechinnovations.model.dine_in.DineInArea

interface DineInAreaDao {
    suspend fun insertArea(area:DineInArea)
    suspend fun getAllArea():List<DineInArea>
    suspend fun getAreaById(id:Int):DineInArea?
    suspend fun deleteAreaById(id:Int)
    suspend fun updateArea(dineInArea: DineInArea)
    suspend fun deleteAllArea()
    suspend fun getAreaByName(name:String):DineInArea?

}