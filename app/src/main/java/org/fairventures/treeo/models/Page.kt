package org.fairventures.treeo.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Page(
    val pageId: Long,
    val pageType: String,
    val questionCode: String,
    var header: Map<String, String>,
    val description: Map<String, String>,
    var options: List<Option>?
) : Parcelable
