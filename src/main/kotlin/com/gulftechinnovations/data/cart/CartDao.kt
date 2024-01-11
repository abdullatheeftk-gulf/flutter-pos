package com.gulftechinnovations.data.cart

import com.gulftechinnovations.model.Cart

interface CartDao {
    suspend fun insertCart(cart: Cart):Int

    suspend fun getAllCarts():List<Cart>

    suspend fun getCartsByUserId(userId:Int):List<Cart>

    suspend fun getCartsByAdminUserId(adminUserId:Int):List<Cart>

    suspend fun getOneCart(invoiceNo:Int):Cart?


    suspend fun updateOneCart(cart: Cart)

    suspend fun deleteOneCart(cartId:Int)
}