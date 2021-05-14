package org.fairventures.treeo.services.models

import com.google.gson.annotations.SerializedName

data class ConfigurationDTO(
    @SerializedName("pages")
    val pages: List<PageDTO>,
    @SerializedName("title")
    val title: Map<String, String>
)