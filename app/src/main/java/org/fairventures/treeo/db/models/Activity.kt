package org.fairventures.treeo.db.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Activity")
data class Activity(
    @PrimaryKey(autoGenerate = true)
    var activityId: Long = 0L,
    var type: String = "",
    var due_date: String,
    var title: String = "",
    var description: String = "",
    var is_complete: Boolean = false,
    var plot: String?,
    var activity_id_from_remoteDB: Long = 0L,
    var activity_code: String = "",
    var questionnaire: Questionnaire? = null
) : Parcelable