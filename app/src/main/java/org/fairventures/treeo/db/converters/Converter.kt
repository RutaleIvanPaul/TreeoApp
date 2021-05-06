package org.fairventures.treeo.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import org.fairventures.treeo.db.models.Answer
import org.fairventures.treeo.db.models.Questionnaire
import java.util.*

class Converter {
    @TypeConverter
    fun fromQuestionnaire(questionnaire: Questionnaire): String {
        return Gson().toJson(questionnaire)
    }

    @TypeConverter
    fun fromString(questionnaire: String): Questionnaire {
        return Gson().fromJson(questionnaire, Questionnaire::class.java)
    }

    @TypeConverter
    fun fromQuestionnaireAnswer(answer: Array<String>): String {
        return Gson().toJson(answer)
    }

    @TypeConverter
    fun fromStringtoAnswer(answer: String): Array<String> {
        return Gson().fromJson(answer, Array<String>::class.java)
    }
}

