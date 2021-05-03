package org.fairventures.treeo.models

data class PlannedActivity (
    val id: Long,
    val dueDate: String,
    val completedByActivity: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val activityTemplate: ActivityTemplate?,
    val plot: Any? = null
)
