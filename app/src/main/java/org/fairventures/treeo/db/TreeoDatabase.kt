package org.fairventures.treeo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.fairventures.treeo.db.dao.ActivityDao
import org.fairventures.treeo.db.models.Activity
import org.fairventures.treeo.db.models.Converter

@Database(
    entities = [Activity::class],
    version = 1
)

@TypeConverters(Converter::class)
abstract class TreeoDatabase: RoomDatabase() {

    abstract fun getActivityDao():ActivityDao
}