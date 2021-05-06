package org.fairventures.treeo.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
import org.fairventures.treeo.R
import org.fairventures.treeo.db.models.*
import org.fairventures.treeo.models.Country
import org.fairventures.treeo.ui.home.HomeViewModel
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

fun insertActivity(homeViewModel:HomeViewModel) {
    val activities = arrayOf(
        Activity(
            type = " ",
            due_date = System.currentTimeMillis(),
            plot = null,
            activity_id_from_remoteDB = 2,
            activity_code = "land",
            questionnaire = Questionnaire(
                activity_id_from_remoteDB = 2,
                questionnaire_id_from_remote = 3,
                questionnaire_title = mapOf("en" to "title", "lg" to "taito"),
                pages = arrayOf(
                    Page(
                        pageType = "checkbox",
                        questionCode = "ac1qc1",
                        header = mapOf("en" to "Activity 1  Page 1 Header", "lg" to "omutwe"),
                        description = mapOf("en" to "Activity 1 Description 1", "lg" to "desc"),
                        options = arrayOf(
                            Option(
                                option_title = mapOf("en" to "Activity 1 Page 1 option 1", "lg" to "oputioni"),
                                option_code = "ac1qc1 option 1"
                            ),
                            Option(
                                option_title = mapOf("en" to "Activity 1 Page 1 option 2", "lg" to "oputioni"),
                                option_code = "ac1qc1 option 2"
                            )
                        )
                    ),
                    Page(
                        pageType = "radio",
                        questionCode = "ac1qc2",
                        header = mapOf("en" to "Activity 1 Page 2 Header", "lg" to "omutwe"),
                        description = mapOf("en" to "description", "lg" to "desc"),
                        options = arrayOf(
                            Option(
                                option_title = mapOf(
                                    "en" to "Activity 1 Page 2 Option 1",
                                    "lg" to "oputioni"
                                ),
                                option_code = "ac1qc2 oputioni 1"
                            ),
                            Option(
                                option_title = mapOf("en" to "Activity 1 Page 2 Option 2", "lg" to "oputioni"),
                                option_code = "ac1qc2 oputioni 2"
                            )
                        )
                    )
                )
            )
        ),
        Activity(
            type = " ",
            due_date = System.currentTimeMillis(),
            plot = null,
            activity_id_from_remoteDB = 3,
            activity_code = "land",
            questionnaire = Questionnaire(
                activity_id_from_remoteDB = 3,
                questionnaire_id_from_remote = 3,
                questionnaire_title = mapOf("en" to "title 1", "lg" to "taito"),
                pages = arrayOf(
                    Page(
                        pageType = "checkbox",
                        questionCode = "ac2qc1",
                        header = mapOf("en" to "Activity 2 Page 1 Header", "lg" to "omutwe"),
                        description = mapOf("en" to "description", "lg" to "desc"),
                        options = arrayOf(
                            Option(
                                option_title = mapOf("en" to "Activity 2 Page 1 Option 1", "lg" to "oputioni"),
                                option_code = "ac2qc1 option 1"
                            ),
                            Option(
                                option_title = mapOf("en" to "Activity 2 Page 1 Option 2", "lg" to "oputioni"),
                                option_code = "ac2qc1 option 2"
                            )
                        )
                    ),
                    Page(
                        pageType = "radio",
                        questionCode = "ac2qc2",
                        header = mapOf("en" to "Activity 2 Page 2 Header", "lg" to "omutwe"),
                        description = mapOf("en" to "description", "lg" to "desc"),
                        options = arrayOf(
                            Option(
                                option_title = mapOf(
                                    "en" to "Activity 2 Page 2 Option 1",
                                    "lg" to "oputioni"
                                ),
                                option_code = "ac2qc2 oputioni 1"
                            ),
                            Option(
                                option_title = mapOf("en" to "Activity 2 Page 2 Option 2", "lg" to "oputioni"),
                                option_code = "ac2qc2 oputioni 2"
                            )
                        )
                    )
                )
            )
        )
    )

    val questionnaireAnswers = arrayOf(
        QuestionnaireAnswer(
            questionnaire_id_from_remote = 2,
            questionCode = "land_use",
            answers = arrayOf("Trees", "Sugarcanes")


        ),
        QuestionnaireAnswer(
            questionnaire_id_from_remote = 2,
            questionCode = "terrain",
            answers = arrayOf("Hilly", "flat")


        ),
        QuestionnaireAnswer(
            questionnaire_id_from_remote = 3,
            questionCode = "land_type",
            answers = arrayOf("Fertile", "Not")


        )
    )

    activities.forEach { activity ->
        homeViewModel.insertActivity(activity)
    }

//        questionnaireAnswers.forEach {
//            homeViewModel.insertQuestionnaireAnswer(it)
//        }
}




