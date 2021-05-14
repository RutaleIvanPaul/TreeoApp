package org.fairventures.treeo.db.models.relations

import androidx.room.Embedded
import androidx.room.Relation
import org.fairventures.treeo.db.models.PageEntity
import org.fairventures.treeo.db.models.QuestionnaireEntity

data class QuestionnaireWithPages(
    @Embedded val questionnaire: QuestionnaireEntity,
    @Relation(parentColumn = "questionnaireId", entityColumn = "questionnaireId")
    val pages: List<PageEntity>
)