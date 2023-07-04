package org.fairventures.treeo.services.models

import com.google.gson.annotations.SerializedName

data class OptionDTO(
    @SerializedName("code")
    val code: String,
    @SerializedName("title")
    val title: Map<String, String>
)