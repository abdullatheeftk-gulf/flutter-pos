package com.gulftechinnovations.data.cart


import com.gulftechinnovations.database.dbQuery
import com.gulftechinnovations.database.resultRowToCart
import com.gulftechinnovations.database.resultRowToProduct
import com.gulftechinnovations.database.tables.CartProductTable
import com.gulftechinnovations.database.tables.CartTable
import com.gulftechinnovations.database.tables.ProductTable
import com.gulftechinnovations.model.Cart
import com.gulftechinnovations.model.CartProduct
import com.gulftechinnovations.model.CartProductItem
import io.ktor.network.selector.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class CartDaoImpl : CartDao {
    override suspend fun insertCart(cart: Cart): Int {
        return dbQuery {
            val cartId = CartTable.insert {
                it[total] = cart.total
                it[totalTaxAmount] = cart.totalTaxAmount
                it[net] = cart.net
                it[customerName] = cart.customerName
                it[info] = cart.info
                it[userId] = cart.userId
                it[adminUserId] = cart.adminUserId
            }[CartTable.id].value

            cart.cartProductItems.forEach { cartProductItem ->
                CartProductTable.insert {
                    it[this.cartId] = cartId
                    it[productId] = cartProductItem.product?.productId!!
                    it[productCartName] = cartProductItem.cartProductName
                    it[productLocalCartName] = cartProductItem.cartProductLocalName
                    it[this.noOfItemsOrdered] = cartProductItem.noOfItemsOrdered
                    it[this.note] = cartProductItem.note
                }
            }

            cartId
        }
    }

    override suspend fun getAllCarts(): List<Cart> {

        return dbQuery {
            val list =
                CartTable.selectAll().orderBy(order = arrayOf(Pair(first = CartTable.id, second = SortOrder.DESC)))
                    .map {
                        CartTable.resultRowToCart(it)
                    }.toList()



            list.map { cart ->
                val cartProductItems = mutableListOf<CartProductItem>()

                val cartProducts: List<CartProduct> = CartProductTable.select {
                    CartProductTable.cartId eq cart.invoiceNo
                }.map { row ->
                    CartProductTable.resultRowToCart(row)
                }

                cartProducts.forEach { cartProduct ->
                    val cartProductItem: CartProductItem

                    val product = ProductTable.select { ProductTable.id eq cartProduct.productId }.map { row ->
                        ProductTable.resultRowToProduct(row)
                    }.singleOrNull()

                    cartProductItem = CartProductItem(
                        noOfItemsOrdered = cartProduct.noOfItemsOrdered,
                        note = cartProduct.note,
                        cartProductName = cartProduct.productCartName,
                        cartProductLocalName = cartProduct.productLocalCartName,
                        product = product

                    )
                    cartProductItems.add(cartProductItem)

                }
                //cart.cartProductItems = cartProductItems
                cart.copy(cartProductItems = cartProductItems)
            }

            list
        }
    }

    override suspend fun getCartsByUserId(userId: Int): List<Cart> {
        return dbQuery {
            val list = CartTable.select(CartTable.userId eq userId).map {
                CartTable.resultRowToCart(it)
            }
            list.map { cart ->
                val cartProductItems = mutableListOf<CartProductItem>()

                val cartProducts: List<CartProduct> = CartProductTable.select {
                    CartProductTable.cartId eq cart.invoiceNo
                }.map { row ->
                    CartProductTable.resultRowToCart(row)
                }

                cartProducts.forEach { cartProduct ->
                    val cartProductItem: CartProductItem

                    val product = ProductTable.select { ProductTable.id eq cartProduct.productId }.map { row ->
                        ProductTable.resultRowToProduct(row)
                    }.singleOrNull()

                    cartProductItem = CartProductItem(
                        noOfItemsOrdered = cartProduct.noOfItemsOrdered,
                        note = cartProduct.note,
                        cartProductName = cartProduct.productCartName,
                        cartProductLocalName = cartProduct.productLocalCartName,
                        product = product

                    )
                    cartProductItems.add(cartProductItem)

                }
                //cart.cartProductItems = cartProductItems
                cart.copy(cartProductItems = cartProductItems)
            }

            list
        }
    }

    override suspend fun getCartsByAdminUserId(adminUserId: Int): List<Cart> {
        return dbQuery {
            val list = CartTable.select(CartTable.userId eq adminUserId).map {
                CartTable.resultRowToCart(it)
            }
            list.map { cart ->
                val cartProductItems = mutableListOf<CartProductItem>()

                val cartProducts: List<CartProduct> = CartProductTable.select {
                    CartProductTable.cartId eq cart.invoiceNo
                }.map { row ->
                    CartProductTable.resultRowToCart(row)
                }

                cartProducts.forEach { cartProduct ->
                    val cartProductItem: CartProductItem

                    val product = ProductTable.select { ProductTable.id eq cartProduct.productId }.map { row ->
                        ProductTable.resultRowToProduct(row)
                    }.singleOrNull()

                    cartProductItem = CartProductItem(
                        noOfItemsOrdered = cartProduct.noOfItemsOrdered,
                        note = cartProduct.note,
                        cartProductName = cartProduct.productCartName,
                        cartProductLocalName = cartProduct.productLocalCartName,
                        product = product

                    )
                    cartProductItems.add(cartProductItem)

                }
                //cart.cartProductItems = cartProductItems
                cart.copy(cartProductItems = cartProductItems)
            }

            list
        }
    }

    override suspend fun getOneCart(invoiceNo: Int): Cart? {
        return dbQuery {
            val cart = CartTable.select { CartTable.id eq invoiceNo }.map {
                CartTable.resultRowToCart(it)
            }.singleOrNull()

            cart?.let { c ->
                val cartProducts = CartProductTable.select { CartProductTable.cartId eq c.invoiceNo }.map { row ->
                    CartProductTable.resultRowToCart(row)
                }
                val cartProductItems = mutableListOf<CartProductItem>()

                cartProducts .forEach {cartProduct->
                   val product =  ProductTable.select { ProductTable.id eq cartProduct.productId }.map {row->
                        ProductTable.resultRowToProduct(row)
                    }.singleOrNull()

                    val cartProductItem = CartProductItem(
                        noOfItemsOrdered = cartProduct.noOfItemsOrdered,
                        note = cartProduct.note,
                        cartProductName = cartProduct.productCartName,
                        cartProductLocalName = cartProduct.productLocalCartName,
                        product = product
                    )
                    cartProductItems.add(cartProductItem)
                }
                //cart.cartProductItems = cartProductItems
                cart.copy(cartProductItems = cartProductItems)

            }

            cart
        }
    }

    override suspend fun updateOneCart(cart: Cart) {
        dbQuery {
            CartTable.update ({ CartTable.id eq cart.invoiceNo }){
                it[this.id] = cart.invoiceNo
                it[this.total] = cart.total
                it[this.totalTaxAmount] = cart.totalTaxAmount
                it[this.net] = cart.net
                it[this.userId] = cart.userId
                it[this.adminUserId] = cart.adminUserId
            }

            CartProductTable.deleteWhere { cartId eq cart.invoiceNo }
            cart.cartProductItems.forEach { cartProductItem ->
                CartProductTable.insert {
                    it[this.cartId] = cartId
                    it[productId] = cartProductItem.product?.productId!!
                    it[productCartName] = cartProductItem.cartProductName
                    it[productLocalCartName] = cartProductItem.cartProductLocalName
                    it[this.noOfItemsOrdered] = cartProductItem.noOfItemsOrdered
                    it[this.note] = cartProductItem.note

                }
            }
        }
    }

    override suspend fun deleteOneCart(cartId: Int) {
        CartTable.deleteWhere {
            CartTable.id eq cartId
        }
    }
}