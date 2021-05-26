package org.fairventures.treeo.repositories

import kotlinx.coroutines.flow.Flow
import org.fairventures.treeo.db.dao.ActivityDao
import org.fairventures.treeo.db.models.ActivityEntity
import org.fairventures.treeo.db.models.OptionEntity
import org.fairventures.treeo.db.models.mappers.ActivityDtoToEntityMapper
import org.fairventures.treeo.db.models.relations.QuestionnaireWithPages
import org.fairventures.treeo.services.models.ActivityDTO
import org.fairventures.treeo.services.models.OptionDTO
import org.fairventures.treeo.services.models.PageDTO
import org.fairventures.treeo.services.models.QuestionnaireDTO
import javax.inject.Inject

class DBMainRepository @Inject constructor(
    private val activityDao: ActivityDao,
    private val mapper: ActivityDtoToEntityMapper
) {
    suspend fun insertActivity(activities: List<ActivityDTO>) {
        activities.forEach {
            val entity = mapper.toEntity(it)
            val activityId = activityDao.insertActivity(entity)
            insertQuestionnaire(activityId, it.activityTemplate.questionnaire)
        }
    }

    private suspend fun insertQuestionnaire(
        activityId: Long,
        questionnaire: QuestionnaireDTO
    ) {
        val entity = mapper.getQuestionnaireEntity(activityId, questionnaire)
        val questionnaireId = activityDao.insertQuestionnaire(entity)
        insertPage(questionnaireId, questionnaire.configuration.pages)
    }

    private suspend fun insertPage(questionnaireId: Long, pages: List<PageDTO>) {
        pages.forEach {
            val entity = mapper.getPageEntity(questionnaireId, it)
            val pageId = activityDao.insertPage(entity)
            insertOption(pageId, it.options)
        }
    }

    private suspend fun insertOption(pageId: Long, options: List<OptionDTO>) {
        options.forEach {
            val entity = mapper.getOptionEntity(pageId, it)
            activityDao.insertOption(entity)
        }
    }

    fun getNextTwoActivities(): Flow<List<ActivityEntity>> {
        return activityDao.getNextTwoActivities()
    }

    suspend fun getQuestionnairePages(activityId: Long): QuestionnaireWithPages {
        return activityDao.getQuestionnairePages(activityId)
    }

    suspend fun getPageOptions(pageId: Long): List<OptionEntity> {
        return activityDao.getPageOptions(pageId)
    }

    suspend fun updateOption(id: Long, isSelected: Boolean) {
        activityDao.updateOption(id, isSelected)
    }

    suspend fun getSelectedOptions(pageId: Long): List<OptionEntity> {
        return activityDao.getSelectedOptions(pageId)
    }

    suspend fun getCompletedActivities(): List<ActivityEntity> {
        return activityDao.getCompletedActivities()
    }

    suspend fun markActivityAsCompleted(id: Long) {
        activityDao.markActivityAsCompleted(id)
    }

//    suspend fun updateActivity(activity: Activity)  = activityDao.updateActivity(activity)
}
