package com.gulftechinnovations.database

import com.gulftechinnovations.database.tables.*
import com.gulftechinnovations.model.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }

fun StudentTable.rowToStudent(row: ResultRow) = Student(
    id = row[id].value,
    name = row[name],
    mark = row[mark]
)

fun UserTable.resultRowToUser(row: ResultRow) = User(
    userId = row[this.id].value,
    userName = row[this.userName],
    userPassword = row[this.userPassword]
)

fun AdminUserTable.resultRowToAdminUser(row: ResultRow) = AdminUser(
    adminId = row[this.id].value,
    adminName = row[this.adminUserName],
    adminPassword = row[this.adminPassword],
    licenseKey = row[this.licenseKey]
)

fun CategoryTable.resultRowToCategory(row: ResultRow)= Category(
    categoryId = row[this.id].value,
    categoryName = row[this.categoryName],
    noOfTimesOrdered = row[this.noOfTimesOrdered],
)

fun SubCategoryTable.resultRowToSubCategory(row: ResultRow)= SubCategory(
    subCategoryId = row[this.id].value,
    categoryId = row[this.categoryId],
    subCategoryName = row[subCategoryName],
    noOfTimesOrdered = row[noOfTimesOrdered]
)

fun ProductTable.resultRowToProduct(row: ResultRow)=Product(
    productId = row[this.id].value,
    productName = row[this.productName],
    productLocalName = row[this.productLocalName],
    productPrice = row[this.productPrice],
    productImage = row[this.productImage],
    noOfTimesOrdered = row[this.noOfTimesOrdered],
    info = row[this.info],
    productTaxInPercentage = row[productTaxInPercentage],
    subCategoryId = row[subCategoryId],
    categories = emptyList()
)

fun MultiProductTable.resultRowToMultiProduct(row: ResultRow)=MultiProduct(
    multiProductId = row[id].value,
    parentProductId = row[parentProductId],
    multiProductName = row[multiProductName],
    multiProductLocalName = row[multiProductLocalName],
    multiProductImage = row[multiProductImage],
    info = row[info]
)

fun CategoryProductTable.resultRowToCategoryProduct(row: ResultRow) = CategoryProduct(
    categoryId = row[categoryId],
    productId = row[productId]
)

fun CartTable.resultRowToCart(row: ResultRow) = Cart(
    invoiceNo = row[id].value,
    total = row[total],
    totalTaxAmount = row[totalTaxAmount],
    net = row[net],
    customerName = row[customerName],
    info  = row[info],
    userId = row[userId],
    adminUserId = row[adminUserId]
)

fun CartProductTable.resultRowToCart(row: ResultRow) = CartProduct(
    cartId =  row[cartId],
    productId = row[productId],
    productCartName = row[productCartName],
    productLocalCartName = row[productLocalCartName],
    noOfItemsOrdered = row[noOfItemsOrdered],
    note = row[note]
)




