package org.fairventures.treeo.models

data class ValidateOTPRegistration(
    val code: String,
    val phoneNumber: String
)