package com.gulftechinnovations.data.admin

import com.gulftechinnovations.model.AdminUser

interface AdminUserDao {
    suspend fun insertAdminUser(adminUser: AdminUser):Int

    suspend fun getOneAdminUser(adminUser: AdminUser):AdminUser?

    suspend fun getOneAdminUserByPassword(adminPassword:String): AdminUser?

    suspend fun getOneAdminUserByName(adminName:String): AdminUser?

    suspend fun getAllAdminUsers():List<AdminUser>

    suspend fun updateAdminUser(adminUser: AdminUser)

    suspend fun deleteAdminUser(adminPassword:String)
}