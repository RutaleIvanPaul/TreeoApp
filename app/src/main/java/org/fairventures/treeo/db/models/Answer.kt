package org.fairventures.treeo.db.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Answer(
    var answer: Array<String>
) : Parcelable
