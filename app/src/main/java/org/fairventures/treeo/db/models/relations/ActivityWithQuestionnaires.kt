package org.fairventures.treeo.db.models.relations

import androidx.room.Embedded
import androidx.room.Relation
import org.fairventures.treeo.db.models.ActivityEntity
import org.fairventures.treeo.db.models.QuestionnaireEntity

data class ActivityWithQuestionnaires(
    @Embedded val activity: ActivityEntity,
    @Relation(parentColumn = "activityId", entityColumn = "activityId")
    val questionnaires: List<QuestionnaireEntity>
)