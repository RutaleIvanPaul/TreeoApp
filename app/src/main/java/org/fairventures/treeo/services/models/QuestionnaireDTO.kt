package org.fairventures.treeo.services.models

import com.google.gson.annotations.SerializedName
import org.fairventures.treeo.services.models.ConfigurationDTO

data class QuestionnaireDTO(
    @SerializedName("id")
    val id: Long,
    @SerializedName("projectID")
    val projectId: Long,
    @SerializedName("configuration")
    val configuration: ConfigurationDTO
)