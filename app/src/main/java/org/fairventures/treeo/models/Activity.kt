package org.fairventures.treeo.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Activity(
    val id: Long,
    val remoteId: Long,
    val dueDate: String,
    val isCompleted: Boolean,
    var title: String,
    val description: String,
    val plot: String? = null,
    val template: ActivityTemplate
) : Parcelable

@Parcelize
data class ActivityTemplate(
    val templateRemoteId: Long,
    var activityType: String,
    val code: Long,
    val preQuestionnaireId: Long?,
    val postQuestionnaireId: Long?
) : Parcelable