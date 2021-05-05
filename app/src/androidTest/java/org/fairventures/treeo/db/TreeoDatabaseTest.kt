package org.fairventures.treeo.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.test.runBlockingTest
import org.fairventures.treeo.AndroidTestCoroutineRule
import org.fairventures.treeo.db.dao.ActivityDao
import org.fairventures.treeo.db.dao.QuestionnaireAnswerDao
import org.fairventures.treeo.db.models.Activity
import org.fairventures.treeo.db.models.Option
import org.fairventures.treeo.db.models.Page
import org.fairventures.treeo.db.models.Questionnaire
import org.fairventures.treeo.getOrAwaitLiveDataValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TreeoDatabaseTest {
    private lateinit var answerDao: QuestionnaireAnswerDao
    private lateinit var activityDao: ActivityDao
    private lateinit var db: TreeoDatabase

    @get:Rule
    var mainCoroutineRule = AndroidTestCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, TreeoDatabase::class.java)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build()
        activityDao = db.getActivityDao()
        answerDao = db.getQuestionnaireAnswerDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDB(){
        db.close()
    }

    @Test
    fun `insertAndGetActivity`() =
        mainCoroutineRule.testDispatcher.runBlockingTest {
            val activity =   Activity(
                type = " ",
                due_date = " ",
                plot = null,
                activity_id_from_remoteDB = 2,
                acitivity_code = "land",
                questionnaire = Questionnaire(
                    activity_id_from_remoteDB = 2,
                    questionnaire_id_from_remote = 3,
                    questionnaire_title = mapOf("en" to "title", "lg" to "taito"),
                    pages = arrayOf(
                        Page(
                            pageType = "checkbox",
                            questionCode = "qc",
                            header = mapOf("en" to "this header", "lg" to "omutwe"),
                            description = mapOf("en" to "description", "lg" to "desc"),
                            options = arrayOf(
                                Option(
                                    option_title = mapOf("en" to "this option", "lg" to "oputioni"),
                                    option_code = "oc"
                                )
                            )
                        )
                    )
                )
            )

            activityDao.insertActivity(activity)

            assertNotNull(activityDao.getActivities().getOrAwaitLiveDataValue())
        }

}