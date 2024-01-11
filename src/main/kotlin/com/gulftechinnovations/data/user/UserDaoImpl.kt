package com.gulftechinnovations.data.user


import com.gulftechinnovations.database.dbQuery
import com.gulftechinnovations.database.resultRowToUser
import com.gulftechinnovations.database.tables.UserTable
import com.gulftechinnovations.model.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserDaoImpl:UserDao {
    override suspend fun insertUser(user: User): Int {
        return dbQuery {
            UserTable.insert{
                it[userName] = user.userName
                it[userPassword] = user.userPassword
            }[UserTable.id].value
        }
    }

    override suspend fun getOneUser(userId: Int): User? {
      return dbQuery {
          UserTable.select {
              UserTable.id eq userId
          }.map {
              UserTable.resultRowToUser(it)
          }.singleOrNull()
      }
    }

    override suspend fun getAllUsers(): List<User> {
        return dbQuery {
            UserTable.selectAll().orderBy(UserTable.id,SortOrder.DESC)
                .map {
                UserTable.resultRowToUser(it)
            }
        }

    }

    override suspend fun updateUser(user: User):Int {
        return dbQuery {
              UserTable.update({
                 UserTable.id eq user.userId
             }){
                 it[userName] = user.userName
                 it[userPassword] = user.userPassword
             }
        }
    }

    override suspend fun deleteUser(userId: Int) {
        dbQuery {
            UserTable.deleteWhere { id eq userId }
        }
    }
}