package org.fairventures.treeo.models

data class RegisterMobileUser(
    val country: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    val phoneNumber: String,
    val username: String
)