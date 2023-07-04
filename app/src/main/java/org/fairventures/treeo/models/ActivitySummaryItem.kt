package org.fairventures.treeo.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ActivitySummaryItem(
    val activity: Activity,
    val pages: List<Page>
):Parcelable

