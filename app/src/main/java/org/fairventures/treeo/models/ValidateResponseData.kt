package org.fairventures.treeo.models

data class ValidateResponseData(
    val errorStatus: String,
    val phoneNumber: String,
    val valid: Boolean
)