package org.fairventures.treeo.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.fairventures.treeo.db.models.QuestionnaireAnswer

@Dao
interface QuestionnaireAnswerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestionnaireAnswer(questionnaireAnswer: QuestionnaireAnswer)

    @Query(
        "SELECT * FROM QuestionnaireAnswer " +
                "WHERE questionnaire_id_from_remote = :questionnaire_id_from_remote"
    )
    fun getActivities(questionnaire_id_from_remote: Long): LiveData<List<QuestionnaireAnswer>>

    @Query(
        "SELECT * FROM QuestionnaireAnswer " +
                "WHERE questionnaire_id_from_remote = :questionnaire_id_from_remote " +
                "AND questionCode = :questionCode"
    )
    suspend fun getAnsweredQuestion(
        questionnaire_id_from_remote: Long,
        questionCode: String
    ): QuestionnaireAnswer
}