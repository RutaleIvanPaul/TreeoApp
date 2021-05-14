package org.fairventures.treeo.services.models

import com.google.gson.annotations.SerializedName

data class ActivityDTO(
    @SerializedName("id")
    val id: Long,
    @SerializedName("dueDate")
    val dueDate: String,
    @SerializedName("completedByActivity")
    val completedByActivity: Boolean,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("plot")
    val plot: String? = null,
    @SerializedName("activityTemplate")
    val activityTemplate: ActivityTemplateDTO,
)
