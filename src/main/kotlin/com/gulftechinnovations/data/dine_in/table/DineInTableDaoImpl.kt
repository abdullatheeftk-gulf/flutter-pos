package com.gulftechinnovations.data.dine_in.table

import com.gulftechinnovations.database.dbQuery
import com.gulftechinnovations.database.resultRowToDineInTable
import com.gulftechinnovations.database.tables.DineInTableTable
import com.gulftechinnovations.model.dine_in.DineInTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DineInTableDaoImpl:DineInTableDao {
    override suspend fun insetTable(dineInTable: DineInTable) {
        dbQuery {
            DineInTableTable.insert {
                it[name] = dineInTable.name
                it[noOfSeats] = dineInTable.noOfSeats
                it[noOfSeatsOccupied] = 0
                it[areaId] = dineInTable.areaId
            }
        }
    }

    override suspend fun getAllTables(): List<DineInTable> {
        return dbQuery {
            DineInTableTable.selectAll().map {
                DineInTableTable.resultRowToDineInTable(it)
            }
        }
    }

    override suspend fun getTablesByAreaId(areaId: Int): List<DineInTable> {
        return  dbQuery {
            DineInTableTable.select { DineInTableTable.areaId eq areaId }.map {
                DineInTableTable.resultRowToDineInTable(it)
            }
        }
    }

    override suspend fun getTablesByNoOfSeats(noOfSeats: Int): List<DineInTable> {
       return  dbQuery {
           DineInTableTable.select{DineInTableTable.noOfSeats eq noOfSeats}.map {
               DineInTableTable.resultRowToDineInTable(it)
           }
       }
    }

    override suspend fun getTablesByNoOfSeatsOccupied(noOfSeatsOccupied: Int): List<DineInTable> {
        return  dbQuery {
            DineInTableTable.select{DineInTableTable.noOfSeatsOccupied eq noOfSeatsOccupied}.map {
                DineInTableTable.resultRowToDineInTable(it)
            }
        }
    }

    override suspend fun getATableById(id: Int): DineInTable? {
        return  dbQuery {
            DineInTableTable.select { DineInTableTable.id eq id }.map {
                DineInTableTable.resultRowToDineInTable(it)
            }.singleOrNull()
        }
    }

    override suspend fun getATableByName(name:String): DineInTable? {
        return dbQuery {
            DineInTableTable.select { DineInTableTable.name eq name }.map {
                DineInTableTable.resultRowToDineInTable(it)
            }.singleOrNull()
        }
    }

    override suspend fun updateATable(dineInTable: DineInTable) {
        return dbQuery {
            DineInTableTable.update({DineInTableTable.id eq dineInTable.id}) {
                it[name] = dineInTable.name
                it[noOfSeats] = dineInTable.noOfSeats
                it[noOfSeatsOccupied] = dineInTable.noOfSeatsOccupied
                it[areaId] = dineInTable.areaId
            }
        }
    }

    override suspend fun deleteATable(id: Int) {
        return dbQuery {
            DineInTableTable.deleteWhere {
                this.id eq id
            }
        }
    }

    override suspend fun deleteAllTablesUnderAnArea(areaId: Int) {
        return dbQuery {
            DineInTableTable.deleteWhere {
                this.areaId eq areaId
            }
        }
    }
}