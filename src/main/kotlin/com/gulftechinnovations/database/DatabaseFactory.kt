package com.gulftechinnovations.database

import com.gulftechinnovations.database.tables.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction


object DatabaseFactory {

    fun init() {

        val driverClassName = "com.mysql.cj.jdbc.Driver"

        val jdbcURL = "jdbc:mysql:///latheef_db?cloudSqlInstance=plucky-bulwark-405311:us-central1:latheef&socketFactory=com.google.cloud.sql.mysql.SocketFactory"

        val database = Database.connect(jdbcURL,driverClassName,"latheef","9526317685")

        transaction(database) {
            SchemaUtils.create(
                StudentTable,
                UserTable,
                AdminUserTable,
                CategoryTable,
                SubCategoryTable,
                ProductTable,
                MultiProductTable,
                CategoryProductTable,
                CartTable,
                CartProductTable,
            )
            /*SchemaUtils.drop(
                UserTable,
                AdminUserTable,
                CategoryTable,
                SubCategoryTable,
                ProductTable,
                MultiProductTable,
                CategoryProductTable,
                CartTable,
                CartProductTable,
            )*/
        }
    }
}