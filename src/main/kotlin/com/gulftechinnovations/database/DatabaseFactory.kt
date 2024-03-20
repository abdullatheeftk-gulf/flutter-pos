package com.gulftechinnovations.database

import com.gulftechinnovations.database.tables.*
import com.gulftechinnovations.database.tables.test_samples.AbTable
import com.gulftechinnovations.database.tables.test_samples.CdTable
import io.ktor.server.config.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.flywaydb.core.Flyway


object DatabaseFactory {

    fun init(config: ApplicationConfig) {

        /*val driverClassName = "com.mysql.cj.jdbc.Driver"

        val jdbcURL = "jdbc:mysql:///latheef_db?cloudSqlInstance=plucky-bulwark-405311:us-central1:latheef&socketFactory=com.google.cloud.sql.mysql.SocketFactory"

        val database = Database.connect(jdbcURL,driverClassName,"latheef","9526317685")*/


        val h2Url = config.property("h2.jdbcURL").getString()
        val h2ServerDriverClassName = config.property("h2.driverClassName").getString()


        val postgresDriverClassName = config.property("postgres.driverClassName").getString()
        val postgresUrl = config.property("postgres.jdbcURL").getString()
        val postgresUser = config.property("postgres.user").getString()
        val postgresPassword = config.property("postgres.password").getString()

        val sqlServerDriverClassName = config.property("sqlserver.driverClassName").getString()
        val sqlServerUrl = config.property("sqlserver.jdbcURL").getString()
        val sqlServerUser = config.property("sqlserver.user").getString()
        val sqlServerPassword = config.property("sqlserver.password").getString()

        try {

            val flyWay = Flyway.configure()
                .dataSource(postgresUrl, postgresUser, postgresPassword)
                .baselineOnMigrate(true)
                .cleanDisabled(false)
                .cleanOnValidationError(true)
                .defaultSchema("public")
                .load()

            flyWay.migrate()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // For h2 database
        /*val database = Database.connect(
            url = h2Url,
            driver = h2ServerDriverClassName,
        )*/

        val database = Database.connect(
            url = postgresUrl,
            driver = postgresDriverClassName,
            user = postgresUser,
            password = postgresPassword
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
                DineInTableTable,
                DineAreaTable,
                CartTableJointTable,
                // Sample
                /*AbTable,
                CdTable*/
            )

           /* SchemaUtils.drop(
                AbTable,
                CdTable,
                CartTableJointTable,
            )*/
        }
    }
}