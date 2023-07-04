package org.fairventures.treeo.models

data class Questionnaire(
    val questionnaireId: Long,
    val pages: List<Page>
)