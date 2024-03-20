package com.gulftechinnovations.data.dine_in.area

import com.gulftechinnovations.database.dbQuery
import com.gulftechinnovations.database.resultRowToDineArea
import com.gulftechinnovations.database.tables.DineAreaTable
import com.gulftechinnovations.model.dine_in.DineInArea
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DineInAreaDaoImpl : DineInAreaDao {
    override suspend fun insertArea(area: DineInArea) {
        dbQuery {
            DineAreaTable.insert {
                it[name] = area.name
            }
        }
    }

    override suspend fun getAllArea(): List<DineInArea> {
        return dbQuery {
            DineAreaTable.selectAll().map {
                DineAreaTable.resultRowToDineArea(it)
            }
        }

    }

    override suspend fun getAreaById(id: Int): DineInArea? {
        return  dbQuery{
            DineAreaTable.select { DineAreaTable.id eq id }.map {
                DineAreaTable.resultRowToDineArea(it)
            }.singleOrNull()
        }
    }

    override suspend fun deleteAreaById(id: Int) {
        dbQuery {
            DineAreaTable.deleteWhere { DineAreaTable.id eq id }
        }

    }

    override suspend fun updateArea(dineInArea: DineInArea) {
        dbQuery {
            DineAreaTable.update({ DineAreaTable.id eq dineInArea.id }){
                it[name] = name
            }
        }

    }

    override suspend fun deleteAllArea() {
        dbQuery {
            DineAreaTable.deleteAll()
        }
    }

    override suspend fun getAreaByName(name: String): DineInArea? {
        return dbQuery {
            DineAreaTable.select { DineAreaTable.name eq name }.map {
                DineAreaTable.resultRowToDineArea(it)
            }.singleOrNull()
        }
    }
}