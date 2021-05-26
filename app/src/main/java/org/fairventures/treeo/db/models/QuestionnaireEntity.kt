package org.fairventures.treeo.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Questionnaire")
data class QuestionnaireEntity(
    @PrimaryKey(autoGenerate = true)
    val questionnaireId: Long?,
    var activityId: Long?,
    val questionnaireRemoteId: Long?,
    val projectId: Long?
)