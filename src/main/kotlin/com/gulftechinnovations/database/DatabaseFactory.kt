package com.gulftechinnovations.database

import com.gulftechinnovations.database.tables.*
import io.ktor.server.config.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction


object DatabaseFactory {

    fun init(config: ApplicationConfig) {

        /*val driverClassName = "com.mysql.cj.jdbc.Driver"

        val jdbcURL = "jdbc:mysql:///latheef_db?cloudSqlInstance=plucky-bulwark-405311:us-central1:latheef&socketFactory=com.google.cloud.sql.mysql.SocketFactory"

        val database = Database.connect(jdbcURL,driverClassName,"latheef","9526317685")*/


        val postgresDriverClassName = config.property("postgres.driverClassName").getString()
        val postgresUrl = config.property("postgres.jdbcURL").getString()
        val postgresUser = config.property("postgres.user").getString()
        val postgresPassword = config.property("postgres.password").getString()

        val sqlServerDriverClassName = config.property("sqlserver.driverClassName").getString()
        val sqlServerUrl = config.property("sqlserver.jdbcURL").getString()
        val sqlServerUser = config.property("sqlserver.user").getString()
        val sqlServerPassword = config.property("sqlserver.password").getString()

        val database = Database.connect(
            url = sqlServerUrl,  // Replace "mydb" with your database name
            driver = sqlServerDriverClassName,
            user = sqlServerUser, // Replace with your PostgreSQL username
            password = sqlServerPassword, // Replace with your PostgreSQL password
        )


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
          /*  SchemaUtils.drop(
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