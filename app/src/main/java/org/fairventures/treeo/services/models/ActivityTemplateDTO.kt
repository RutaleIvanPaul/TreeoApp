package org.fairventures.treeo.services.models

import com.google.gson.annotations.SerializedName

data class ActivityTemplateDTO(
    @SerializedName("id")
    val id: Long,
    @SerializedName("activityType")
    val activityType: String,
    @SerializedName("code")
    val code: Long,
    @SerializedName("pre_questionnaireID")
    val preQuestionnaireId: Long,
    @SerializedName("post_questionnaireID")
    val postQuestionnaireId: Long,
    @SerializedName("questionnaire")
    val questionnaire: QuestionnaireDTO
)