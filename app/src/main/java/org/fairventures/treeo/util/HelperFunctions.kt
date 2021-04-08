package org.fairventures.treeo.util

import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

fun getErrorMessageFromJson(response: String): String {
    val obj = JSONObject(response)
    val gson = Gson()

    return when (obj["message"]) {
        is JSONArray -> {
            val messageList = gson.fromJson(obj["message"].toString(), List::class.java)
            messageList[0].toString()
        }
        else -> {
            obj["message"].toString()
        }
    }
}