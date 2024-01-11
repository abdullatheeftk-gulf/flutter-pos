package com.gulftechinnovations.data.product


import com.gulftechinnovations.database.dbQuery
import com.gulftechinnovations.database.resultRowToCategoryProduct
import com.gulftechinnovations.database.resultRowToProduct
import com.gulftechinnovations.database.tables.CategoryProductTable
import com.gulftechinnovations.database.tables.ProductTable
import com.gulftechinnovations.model.Product
import io.ktor.server.plugins.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class ProductDaoImpl : ProductDao {
    override suspend fun insertProduct(product: Product): Int {
        return dbQuery {

           val newProductId =  ProductTable.insert {
                it[productName] = product.productName
                it[productLocalName] = product.productLocalName
                it[productPrice] = product.productPrice
                it[productImage] = product.productImage
                it[productTaxInPercentage] = product.productTaxInPercentage
                it[info] = product.info
                it[this.noOfTimesOrdered] = product.noOfTimesOrdered
                it[subCategoryId] = product.subCategoryId
            }[ProductTable.id].value

            product.categories.forEach {id->
                CategoryProductTable.insert {
                    it[categoryId]  = id
                    it[productId] =newProductId
                }
            }

             newProductId


        }
    }

    override suspend fun getAllProducts(
        sortOrderById: Pair<Column<EntityID<Int>>, SortOrder>?,
        sortOrderByProductName: Pair<Column<String>, SortOrder>?,
        sortOrderByNoOfTimesOrdered: Pair<Column<Int>, SortOrder>?,
        sortOrderByProductPrice: Pair<Column<Float>, SortOrder>?
    ): List<Product> {
        return dbQuery {
            if (sortOrderByNoOfTimesOrdered != null) {
                ProductTable.selectAll().orderBy(sortOrderByNoOfTimesOrdered).map {
                    ProductTable.resultRowToProduct(it)
                }
            } else {
                ProductTable.selectAll().map {
                    ProductTable.resultRowToProduct(it)
                }
            }
        }

    }

    override suspend fun getProductsByCategoryId(
        categoryId: Int,
        sortOrderById: Pair<Column<EntityID<Int>>, SortOrder>?,
        sortOrderByProductName: Pair<Column<String>, SortOrder>?,
        sortOrderByNoOfTimesOrdered: Pair<Column<Int>, SortOrder>?,
        sortOrderByProductPrice: Pair<Column<Float>, SortOrder>?
    ): List<Product> {

        return dbQuery {
            val products = mutableListOf<Product>()
            val categoryProduct = CategoryProductTable.select { CategoryProductTable.categoryId eq categoryId }.map {
                CategoryProductTable.resultRowToCategoryProduct(it)
            }
            categoryProduct.forEach {
                val product = ProductTable.select(ProductTable.id eq it.productId).map { resultRow ->
                    ProductTable.resultRowToProduct(resultRow)
                }.single()
                products.add(product)
            }
            products
        }
    }

    override suspend fun getProductsById(productId: Int): Product? {
        return  dbQuery {
            ProductTable.select(ProductTable.id eq productId).map { resultRow ->
                ProductTable.resultRowToProduct(resultRow)
            }.singleOrNull()
        }
    }

    override suspend fun getAProductNameById(productId: Int): String {
        return  dbQuery {
            ProductTable.select { ProductTable.id eq productId }.map {
                it[ProductTable.productName]
            }.singleOrNull() ?: throw  BadRequestException("Invalid product Id")
        }
    }

    override suspend fun searchProductsByName(
        keyWord: String,
        sortOrderById: Pair<Column<EntityID<Int>>, SortOrder>?,
        sortOrderByProductName: Pair<Column<String>, SortOrder>?,
        sortOrderByNoOfTimesOrdered: Pair<Column<Float>, SortOrder>?,
        sortOrderByProductPrice: Pair<Column<Float>, SortOrder>?
    ): List<Product> {
        return dbQuery {
            ProductTable.select{
                ProductTable.productName.lowerCase() like keyWord.lowercase()
            }.map {
                ProductTable.resultRowToProduct(it)
            }
        }
    }

    override suspend fun updateAProduct(product: Product) {
        dbQuery {
            ProductTable.update({ProductTable.id eq product.productId}) {
                it[id] = product.productId
                it[productName] = product.productName
                it[productPrice] = product.productPrice
                it[productImage] = product.productImage
                it[productTaxInPercentage] = product.productTaxInPercentage
                it[noOfTimesOrdered] = product.noOfTimesOrdered
                it[info] = product.info

            }
        }
    }

    override suspend fun updateAProductNoOfItemsOrdered(productId: Int,increment:Float) {
        dbQuery {
            val noOfItemsOrdered = ProductTable.select { ProductTable.id eq productId }.map {
                it[ProductTable.noOfTimesOrdered]
            }.single()
            ProductTable.update({ProductTable.id eq productId}) {
                it[this.noOfTimesOrdered] = noOfItemsOrdered+increment
            }
        }
    }

    override suspend fun updateAProductPhoto(
        productId:Int,
        productName:String
    ){
        dbQuery {

            ProductTable.update ({ ProductTable.id eq productId }){
                it[productImage] = productName
            }
        }
    }

    override suspend fun deleteAProduct(productId: Int) {
        dbQuery {
            ProductTable.deleteWhere {
                ProductTable.id eq productId
            }
        }
    }


}