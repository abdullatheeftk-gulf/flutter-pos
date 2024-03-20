package com.gulftechinnovations.data.sample

import com.gulftechinnovations.database.dbQuery
import com.gulftechinnovations.database.resultRowToAbTable
import com.gulftechinnovations.database.tables.test_samples.AbTable
import com.gulftechinnovations.model.test_samples.Ab
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

class AbDaoImpl:AbDao {
    override suspend fun insertAb(ab: Ab) {
        dbQuery {
            AbTable.insert {
                it[name] = ab.name
            }
        }
    }

    override suspend fun getAllAb(): List<Ab> {
       return dbQuery {
           AbTable.selectAll().map {
               AbTable.resultRowToAbTable(it)
           }
       }
    }

    override suspend fun updateAb(ab: Ab) {
        dbQuery {
            AbTable.update({AbTable.id eq ab.id}) {
                it[name] = ab.name
            }
        }
    }

    override suspend fun deleteOneAb(id: Int) {
        dbQuery {
            AbTable.deleteWhere {
                AbTable.id eq id
            }
        }
    }
}