package org.fairventures.treeo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import org.fairventures.treeo.db.dao.ActivityDao
import org.fairventures.treeo.db.dao.LandSurveyDao
import org.fairventures.treeo.db.models.*

//@TypeConverters(Converter::class)
@Database(
    entities = [
        ActivityEntity::class,
        OptionEntity::class,
        PageEntity::class,
        QuestionnaireEntity::class,
        LandSurvey::class
    ],
    version = 1
)
abstract class TreeoDatabase : RoomDatabase() {

    abstract fun getActivityDao(): ActivityDao

    abstract fun getLandSurveyDao(): LandSurveyDao

//    abstract fun getQuestionnaireAnswerDao(): QuestionnaireAnswerDao

}
