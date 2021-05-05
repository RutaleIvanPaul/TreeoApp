package org.fairventures.treeo.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "QuestionnaireAnswer")
data class QuestionnaireAnswer (
    @PrimaryKey(autoGenerate = true)
    var questionnaire_answer_id: Long = 0L,
    var questionnaire_id_from_remote: Long,
    var questionCode: String,
    var answers: Array<String>
)