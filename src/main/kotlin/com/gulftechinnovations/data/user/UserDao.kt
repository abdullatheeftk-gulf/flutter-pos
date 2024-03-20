package com.gulftechinnovations.data.user

import com.gulftechinnovations.model.User

interface UserDao {
    suspend fun insertUser(user: User):Int

    suspend fun getOneUser(userPassword:String):User?

    suspend fun getAllUsers():List<User>

    suspend fun updateUser(oldPassword:String,newUser: User):String

    suspend fun deleteUser(userPassword: String)

}