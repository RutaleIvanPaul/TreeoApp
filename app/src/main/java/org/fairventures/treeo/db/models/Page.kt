package org.fairventures.treeo.db.models

data class Page (
    var pageType: String,
    var questionCode: String,
    var header: Map<String,String>,
    var description: Map<String,String>,
    var options: Array<Option>
)