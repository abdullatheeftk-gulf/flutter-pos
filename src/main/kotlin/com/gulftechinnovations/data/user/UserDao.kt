package com.gulftechinnovations.data.user

import com.gulftechinnovations.model.User

interface UserDao {
    suspend fun insertUser(user: User):Int

    suspend fun getOneUser(userPassword:String):User?

    suspend fun getAllUsers():List<User>

    suspend fun updateUser(oldPassword:String,newPassword:String):String

    suspend fun deleteUser(userPassword: String)

}