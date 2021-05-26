package org.fairventures.treeo.services.models

import com.google.gson.annotations.SerializedName
import org.fairventures.treeo.services.models.OptionDTO

data class PageDTO(
    @SerializedName("header")
    val header: Map<String, String>,
    @SerializedName("options")
    val options: List<OptionDTO>,
    @SerializedName("pageType")
    val pageType: String,
    @SerializedName("description")
    val description: Map<String, String>,
    @SerializedName("questionCode")
    val questionCode: String
)