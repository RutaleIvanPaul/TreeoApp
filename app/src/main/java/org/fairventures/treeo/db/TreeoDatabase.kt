package org.fairventures.treeo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import org.fairventures.treeo.db.dao.ActivityDao
import org.fairventures.treeo.db.models.ActivityEntity
import org.fairventures.treeo.db.models.OptionEntity
import org.fairventures.treeo.db.models.PageEntity
import org.fairventures.treeo.db.models.QuestionnaireEntity

//@TypeConverters(Converter::class)
@Database(
    entities = [
        ActivityEntity::class,
        OptionEntity::class,
        PageEntity::class,
        QuestionnaireEntity::class
    ],
    version = 1
)
abstract class TreeoDatabase : RoomDatabase() {

    abstract fun getActivityDao(): ActivityDao

//    abstract fun getQuestionnaireAnswerDao(): QuestionnaireAnswerDao

}
