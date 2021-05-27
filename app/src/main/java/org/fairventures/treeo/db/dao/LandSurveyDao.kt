package org.fairventures.treeo.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import org.fairventures.treeo.db.models.LandSurvey

@Dao
interface LandSurveyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLandSurveyItem(landSurvey: LandSurvey)
}