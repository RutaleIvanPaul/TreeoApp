package org.fairventures.treeo.repository

import org.fairventures.treeo.db.dao.ActivityDao
import org.fairventures.treeo.db.models.Activity
import javax.inject.Inject

class DBMainRepository @Inject constructor(
    private val activityDao: ActivityDao
) {
    suspend fun insertActivity(activity: Activity) =
        activityDao.insertActivity(activity)

    fun getActivities() =
        activityDao.getActivities()
}