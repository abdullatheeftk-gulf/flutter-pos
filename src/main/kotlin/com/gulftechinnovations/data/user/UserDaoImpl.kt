package com.gulftechinnovations.data.user


import com.gulftechinnovations.database.dbQuery
import com.gulftechinnovations.database.resultRowToUser
import com.gulftechinnovations.database.tables.UserTable
import com.gulftechinnovations.model.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserDaoImpl : UserDao {
    override suspend fun insertUser(user: User): Int {
        return dbQuery {
            UserTable.insert {
                it[userPassword] = user.userPassword
                it[userName] = user.userName
            }[UserTable.id].value
        }
    }

    override suspend fun getOneUser(userPassword: String): User? {
        return dbQuery {
            UserTable.select {
                UserTable.userPassword eq userPassword
            }.map {
                UserTable.resultRowToUser(it)
            }.singleOrNull()
        }
    }

    override suspend fun getAllUsers(): List<User> {
        return dbQuery {
            UserTable.selectAll()
                .map {
                    UserTable.resultRowToUser(it)
                }
        }

    }

    override suspend fun updateUser(oldPassword: String, newUser: User): String {
        return dbQuery {
            UserTable.update({
                UserTable.userPassword eq oldPassword
            }) {
                it[userPassword] = newUser.userPassword
                it[id] = newUser.userId
                it[userName] = newUser.userName
            }
            "password updated"
        }
    }

    override suspend fun deleteUser(userPassword: String) {
        dbQuery {
            UserTable.deleteWhere {
                this.userPassword eq userPassword
            }
        }
    }
}