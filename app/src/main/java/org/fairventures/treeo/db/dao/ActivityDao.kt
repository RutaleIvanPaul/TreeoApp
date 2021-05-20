package org.fairventures.treeo.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.fairventures.treeo.db.models.Activity

@Dao
interface ActivityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: Activity)

    @Query("SELECT * FROM Activity")
    fun getActivities(): LiveData<List<Activity>>

    @Query("SELECT * FROM Activity WHERE is_complete = 1")
    suspend fun getCompletedActivities(): List<Activity>

    @Query("SELECT * FROM Activity WHERE is_complete = 0 LIMIT 2")
    suspend fun getNextTwoActivities(): List<Activity>

    @Update
    suspend fun updateActivity(activity: Activity)

}