package com.gulftechinnovations.data.sample

import com.gulftechinnovations.database.dbQuery
import com.gulftechinnovations.database.resultRowToCdTable
import com.gulftechinnovations.database.tables.test_samples.CdTable
import com.gulftechinnovations.model.test_samples.Cd
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

class CdDaoImpl:CdDao {
    override suspend fun insertCd(cd: Cd) {
        dbQuery {
            CdTable.insert {
                it[name] = cd.name
                it[price] = cd.price
                it[abId] = cd.abId
            }
        }
    }

    override suspend fun getAllCd(): List<Cd> {
        return dbQuery {
            CdTable.selectAll().map {
                CdTable.resultRowToCdTable(it)
            }
        }
    }

    override suspend fun updateACd(cd: Cd) {
        dbQuery {
            CdTable.update({CdTable.id eq cd.id}) {
                it[name] = cd.name
                it[price] = cd.price
                it[abId] = cd.abId
            }
        }
    }

    override suspend fun deleteACd(id: Int) {
        dbQuery {
            CdTable.deleteWhere {
                CdTable.id eq id
            }
        }
    }
}