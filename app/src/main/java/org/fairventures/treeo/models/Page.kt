package org.fairventures.treeo.models

data class Page (
    val header: Map<String, String>,
    val options: List<Option>,
    val pageType: String,
    val description: Map<String, String>,
    val questionCode: String
)