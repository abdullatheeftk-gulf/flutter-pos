package com.gulftechinnovations.data.user

import com.gulftechinnovations.model.User

interface UserDao {
    suspend fun insertUser(user: User):Int

    suspend fun getOneUser(userId:Int):User?

    suspend fun getAllUsers():List<User>

    suspend fun updateUser(user: User):Int

    suspend fun deleteUser(userId: Int)

}