package com.gulftechinnovations.data.admin

import com.gulftechinnovations.database.dbQuery
import com.gulftechinnovations.database.resultRowToAdminUser
import com.gulftechinnovations.database.tables.AdminUserTable
import com.gulftechinnovations.database.tables.UserTable
import com.gulftechinnovations.model.AdminUser

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class AdminUserDaoImpl : AdminUserDao {
    override suspend fun insertAdminUser(adminUser: AdminUser): Int {
        return dbQuery {
            AdminUserTable.insert {
                it[adminUserName] = adminUser.adminName
                it[adminPassword] = adminUser.adminPassword
                it[licenseKey] = adminUser.licenseKey
            }[AdminUserTable.id].value
        }
    }

    override suspend fun getOneAdminUser(adminUser: AdminUser): AdminUser? {
        return  dbQuery {
            AdminUserTable.select {
                (AdminUserTable.adminUserName eq adminUser.adminName) and (AdminUserTable.adminPassword eq adminUser.adminPassword)
            }.map {
                AdminUserTable.resultRowToAdminUser(it)
            }.singleOrNull()
        }
    }

    override suspend fun getOneAdminUserByPassword(adminPassword:String): AdminUser? {
        return dbQuery {
            AdminUserTable.select {
                AdminUserTable.adminPassword eq adminPassword
            }.map {
                AdminUserTable.resultRowToAdminUser(it)
            }.singleOrNull()
        }
    }

    override suspend fun getOneAdminUserByName(adminName:String): AdminUser? {
        return  dbQuery {
            AdminUserTable.select {
                (AdminUserTable.adminUserName eq adminName)
            }.map {
                AdminUserTable.resultRowToAdminUser(it)
            }.singleOrNull()
        }
    }

    override suspend fun getAllAdminUsers(): List<AdminUser> {
        return dbQuery {
            AdminUserTable.selectAll().map {
                AdminUserTable.resultRowToAdminUser(it)
            }
        }
    }

    override suspend fun updateAdminUser(adminUser: AdminUser) {
        dbQuery {
            AdminUserTable.update({ AdminUserTable.id eq adminUser.adminId }) {
                it[adminUserName] = adminUser.adminName
                it[adminPassword] = adminUser.adminPassword
                it[licenseKey] = adminUser.licenseKey
                it[id] = adminUser.adminId
            }
        }
    }

    override suspend fun deleteAdminUser(adminPassword: String) {
        dbQuery {
            AdminUserTable.deleteWhere {
                AdminUserTable.adminPassword eq adminPassword
            }
        }
    }


}