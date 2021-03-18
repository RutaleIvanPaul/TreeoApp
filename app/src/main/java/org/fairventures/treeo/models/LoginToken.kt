package org.fairventures.treeo.models

data class LoginToken(
    val userName: String,
    val email: String,
    val token: String,
    val status: Int
)
