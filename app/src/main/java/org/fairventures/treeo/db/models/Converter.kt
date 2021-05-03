package org.fairventures.treeo.db.models

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converter {

    @TypeConverter
    fun fromQuestionnaire(questionnaire: Questionnaire): String {
        return Gson().toJson(questionnaire)
    }

    @TypeConverter
    fun fromString(questionnaire: String): Questionnaire {
        return Gson().fromJson(questionnaire,Questionnaire::class.java)
    }

}