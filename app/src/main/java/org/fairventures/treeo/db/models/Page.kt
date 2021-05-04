package org.fairventures.treeo.db.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Page(
    var pageType: String,
    var questionCode: String,
    var header: Map<String, String>,
    var description: Map<String, String>,
    var options: Array<Option>
) : Parcelable