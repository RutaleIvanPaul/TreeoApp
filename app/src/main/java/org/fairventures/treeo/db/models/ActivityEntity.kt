package org.fairventures.treeo.db.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Activity")
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true)
    val activityId: Long?,
    val activityRemoteId: Long?,
    val dueDate: String?,
    val isComplete: Boolean = false,
    val title: String?,
    val description: String?,
    val plot: String?,
    @Embedded val template: ActivityTemplate
)

data class ActivityTemplate(
    val templateRemoteId: Long?,
    val activityType: String?,
    val code: Long?,
    val preQuestionnaireId: Long?,
    val postQuestionnaireId: Long?
)