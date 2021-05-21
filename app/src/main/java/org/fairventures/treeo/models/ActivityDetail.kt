package org.fairventures.treeo.models

import org.fairventures.treeo.db.models.Activity

data class ActivityDetail(
    val activity: Activity,
    val questionAnswer: Map<String, String>
)

