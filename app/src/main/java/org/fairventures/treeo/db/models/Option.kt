package org.fairventures.treeo.db.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Option(
    var option_title: Map<String, String>,
    var option_code: String
) : Parcelable