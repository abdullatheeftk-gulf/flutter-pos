package com.gulftechinnovations.util


 data class CustomHttpException(
    val errorMessage:String,
    val code:Int
):Exception()