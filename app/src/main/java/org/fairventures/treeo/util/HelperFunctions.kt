package org.fairventures.treeo.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
import org.fairventures.treeo.R
import org.fairventures.treeo.models.Country
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

fun getCountries(): MutableList<Country> {
    return mutableListOf(
        Country(R.drawable.uganda_flag, "Uganda", "+256"),
        Country(R.drawable.indonesia_flag, "Indonesia", "+62"),
        Country(R.drawable.czech_republic_flag, "Czech Republic", "+420"),
        Country(R.drawable.germany_flag, "Germany", "+49"),
    )
}

fun closeKeyboard(view: View, context: Context) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun showView(view: View) {
    view.visibility = View.VISIBLE
}

fun hideView(view: View) {
    view.visibility = View.GONE
}

fun enableView(view: View) {
    view.isEnabled = true
}

fun disableView(view: View) {
    view.isEnabled = false
}
