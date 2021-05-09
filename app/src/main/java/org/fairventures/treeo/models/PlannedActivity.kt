package org.fairventures.treeo.models

data class PlannedActivity (
    val id: Long,
    val dueDate: String,
    val completedByActivity: Boolean,
    val title:String,
    val description: String,
    val createdAt: String,
    val updatedAt: String,
    val activityTemplate: ActivityTemplate?,
    val plot: Any? = null
)
