package org.fairventures.treeo.repository

import org.fairventures.treeo.db.dao.ActivityDao
import org.fairventures.treeo.db.dao.QuestionnaireAnswerDao
import org.fairventures.treeo.db.models.Activity
import org.fairventures.treeo.db.models.QuestionnaireAnswer
import javax.inject.Inject

class DBMainRepository @Inject constructor(
    private val activityDao: ActivityDao,
    private val questionnaireAnswerDao: QuestionnaireAnswerDao
) {
    suspend fun insertActivity(activity: Activity) =
        activityDao.insertActivity(activity)

    fun getActivities() = activityDao.getActivities()

    suspend fun insertQuestionnaireAnswer(questionnaireAnswer: QuestionnaireAnswer) =
        questionnaireAnswerDao.insertQuestionnaireAnswer(questionnaireAnswer)

    fun getQuestionnaireAnswers(questionnaire_id_from_remote: Long) =
        questionnaireAnswerDao.getActivities(questionnaire_id_from_remote)

    fun getAnsweredQuestion(questionnaire_id_from_remote: Long, questionCode: String) =
        questionnaireAnswerDao.getAnsweredQuestion(questionnaire_id_from_remote, questionCode)

    suspend fun getNextTwoActivities() = activityDao.getNextTwoActivities()

    suspend fun getCompletedActivities() = activityDao.getCompletedActivities()

    suspend fun updateActivity(activity: Activity) = activityDao.updateActivity(activity)
}
