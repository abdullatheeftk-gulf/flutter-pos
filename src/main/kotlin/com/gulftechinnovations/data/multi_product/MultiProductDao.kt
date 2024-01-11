package com.gulftechinnovations.data.multi_product

import com.gulftechinnovations.model.MultiProduct

interface MultiProductDao {
    suspend fun insertMultiProduct(multiProduct: MultiProduct):Int

    suspend fun getMultiProductsForAProductId(parentProductId: Int):List<MultiProduct>

    suspend fun updateAMultiProduct(multiProduct: MultiProduct)

    suspend fun deleteAMultiProduct(multiProductId: Int)
}