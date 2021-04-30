package org.fairventures.treeo.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Activity")
data class Activity(
    @PrimaryKey(autoGenerate = true)
    var activityId: Long = 0L,
    var type: String = "",
    var due_date: String = "",
    var plot: String?,
    var activity_id_from_remoteDB: Long = 0L,
    var acitivity_code: String = "",
    var questionnaire: Questionnaire? = null
)