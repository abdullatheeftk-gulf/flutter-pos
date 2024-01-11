package com.gulftechinnovations.translation

import com.deepl.api.Translator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun deepLTranslate(text:String):String{
   return withContext(Dispatchers.IO) {
        try {
            val authKey = "BTLYknTzACgcZwf3"
            val translator = Translator(authKey)
            val result = translator.translateText(text, null, "ar")
             result.text
        } catch (e: Exception) {
             e.message ?: "Some error"
        }
    }
}