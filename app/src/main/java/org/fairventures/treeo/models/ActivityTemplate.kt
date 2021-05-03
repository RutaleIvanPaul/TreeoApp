package org.fairventures.treeo.models


data class ActivityTemplate (
    val id: Long,
    val activityType: String,
    val code: Long,
    val preQuestionnaireID: Long,
    val postQuestionnaireID: Long,
    val questionnaire: Questionnaire
)