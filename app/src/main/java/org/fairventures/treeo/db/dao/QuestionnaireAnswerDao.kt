package org.fairventures.treeo.db.dao

import androidx.room.Dao

@Dao
interface QuestionnaireAnswerDao {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertQuestionnaireAnswer(questionnaireAnswer: QuestionnaireAnswer)
//
//    @Query(
//        "SELECT * FROM QuestionnaireAnswer " +
//                "WHERE questionnaire_id_from_remote = :questionnaire_id_from_remote")
//    fun getActivities(questionnaire_id_from_remote: Long): LiveData<List<QuestionnaireAnswer>>
//
//    @Query(
//        "SELECT * FROM QuestionnaireAnswer " +
//                "WHERE questionnaire_id_from_remote = :questionnaire_id_from_remote " +
//                "AND questionCode = :questionCode")
//    fun getAnsweredQuestion(
//        questionnaire_id_from_remote: Long, questionCode: String):LiveData<QuestionnaireAnswer>
}