package org.fairventures.treeo.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Option(
    val optionId: Long,
    val pageId: Long,
    val code: String,
    var isSelected: Boolean,
    var title: Map<String, String>
) : Parcelable