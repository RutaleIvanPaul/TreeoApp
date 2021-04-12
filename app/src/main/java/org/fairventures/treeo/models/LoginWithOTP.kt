package org.fairventures.treeo.models

data class LoginWithOTP(
    val phoneNumber: String,
    val otp: String
)