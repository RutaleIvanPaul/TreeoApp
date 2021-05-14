package org.fairventures.treeo.db.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.fairventures.treeo.db.models.ActivityEntity
import org.fairventures.treeo.db.models.OptionEntity
import org.fairventures.treeo.db.models.PageEntity
import org.fairventures.treeo.db.models.QuestionnaireEntity
import org.fairventures.treeo.db.models.relations.QuestionnaireWithPages

@Dao
interface ActivityDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertActivity(activity: ActivityEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQuestionnaire(questionnaire: QuestionnaireEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPage(page: PageEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOption(option: OptionEntity)

    @Query("SELECT * FROM Activity")
    fun getActivities(): Flow<List<ActivityEntity>>

    @Query("SELECT * FROM Activity WHERE isComplete = 0 LIMIT 2")
    fun getNextTwoActivities(): Flow<List<ActivityEntity>>

    @Query("SELECT * FROM Questionnaire WHERE activityId=:id")
    suspend fun getQuestionnairePages(id: Long): QuestionnaireWithPages

    @Query("SELECT * FROM Option WHERE pageId=:id")
    suspend fun getPageOptions(id: Long): List<OptionEntity>

    @Query("SELECT * FROM Activity WHERE isComplete = 1")
    suspend fun getCompletedActivities(): List<ActivityEntity>

    @Query("UPDATE Option SET isSelected=:isSelected WHERE optionId=:id")
    suspend fun updateOption(id: Long, isSelected: Boolean)

    @Update
    suspend fun updateActivity(activity: ActivityEntity)

    @Query("SELECT * FROM Option WHERE isSelected=1 AND pageId = :pageId")
    suspend fun getSelectedOptions(pageId: Long): List<OptionEntity>

    @Query("UPDATE Activity SET isComplete = 1 WHERE activityId = :id")
    suspend fun markActivityAsCompleted(id: Long)

}
