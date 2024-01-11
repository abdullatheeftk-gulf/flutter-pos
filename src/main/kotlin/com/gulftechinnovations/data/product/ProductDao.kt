package com.gulftechinnovations.data.product

import com.gulftechinnovations.database.tables.ProductTable
import com.gulftechinnovations.model.Product
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SortOrder

interface ProductDao {

    suspend fun insertProduct(product: Product): Int

    suspend fun getAllProducts(
        sortOrderById: Pair<Column<EntityID<Int>>, SortOrder>? = null,
        sortOrderByProductName: Pair<Column<String>, SortOrder>? = null,
        sortOrderByNoOfTimesOrdered: Pair<Column<Int>, SortOrder>? = null,
        sortOrderByProductPrice: Pair<Column<Float>, SortOrder>? = null
    ): List<Product>

    suspend fun getProductsByCategoryId(
        categoryId: Int,
        sortOrderById: Pair<Column<EntityID<Int>>, SortOrder>? = null,
        sortOrderByProductName: Pair<Column<String>, SortOrder>? = null,
        sortOrderByNoOfTimesOrdered: Pair<Column<Int>, SortOrder>? = null,
        sortOrderByProductPrice: Pair<Column<Float>, SortOrder>? = null
    ): List<Product>

    suspend fun getProductsById(productId: Int): Product?

    suspend fun getAProductNameById(productId:Int) : String

    suspend fun searchProductsByName(
        keyWord: String, sortOrderById: Pair<Column<EntityID<Int>>, SortOrder>? = null,
        sortOrderByProductName: Pair<Column<String>, SortOrder>? = null,
        sortOrderByNoOfTimesOrdered: Pair<Column<Float>, SortOrder>? = Pair(ProductTable.noOfTimesOrdered,SortOrder.DESC),
        sortOrderByProductPrice: Pair<Column<Float>, SortOrder>? = null
    ): List<Product>

    suspend fun updateAProduct(
        product: Product
    )

    suspend fun updateAProductNoOfItemsOrdered(
        productId:Int,
        increment:Float
    )

    suspend fun updateAProductPhoto(
        productId:Int,
        productName:String
    )

    suspend fun deleteAProduct(productId: Int)


}