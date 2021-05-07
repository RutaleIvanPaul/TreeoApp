package org.fairventures.treeo.models

data class Question(val type: String, val title: String, val list: List<Option>)

data class Option(
    var title: Map<String, String>,
    var code: String
)