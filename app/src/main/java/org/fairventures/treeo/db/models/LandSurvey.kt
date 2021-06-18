package org.fairventures.treeo.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LandSurvey")
data class LandSurvey(
    @PrimaryKey(autoGenerate = true)
    val landsurveyID: Long?,
    val sequenceNumber: Int,
    val imagePath: String,
    val imageType: String
)
