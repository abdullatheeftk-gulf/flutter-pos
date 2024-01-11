package com.gulftechinnovations.data.multi_product


import com.gulftechinnovations.database.dbQuery
import com.gulftechinnovations.database.resultRowToMultiProduct
import com.gulftechinnovations.database.tables.MultiProductTable
import com.gulftechinnovations.model.MultiProduct
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

class MultiProductDaoImpl:MultiProductDao {
    override suspend fun insertMultiProduct(multiProduct: MultiProduct) :Int{
        return dbQuery {
            MultiProductTable.insert {
                it[parentProductId] = multiProduct.parentProductId
                it[multiProductName] = multiProduct.multiProductName
                it[multiProductImage] = multiProduct.multiProductImage
                it[info]= multiProduct.info
            }[MultiProductTable.id].value
        }
    }

    override suspend fun getMultiProductsForAProductId(parentProductId: Int): List<MultiProduct> {
        return dbQuery {
            MultiProductTable.select(MultiProductTable.parentProductId eq parentProductId).map {
                MultiProductTable.resultRowToMultiProduct(it)
            }
        }
    }

    override suspend fun updateAMultiProduct(multiProduct: MultiProduct) {
        dbQuery {
            MultiProductTable.update({MultiProductTable.id eq multiProduct.multiProductId}) {
                it[id] = multiProduct.multiProductId
                it[parentProductId] = multiProduct.parentProductId
                it[multiProductName] = multiProduct.multiProductName
                it[multiProductImage] = multiProduct.multiProductImage
                it[info]= multiProduct.info
            }
        }
    }

    override suspend fun deleteAMultiProduct(multiProductId: Int) {
        dbQuery {
            MultiProductTable.deleteWhere {
                id eq multiProductId
            }
        }
    }
}