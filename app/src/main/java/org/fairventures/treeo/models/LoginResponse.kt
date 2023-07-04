package org.fairventures.treeo.models

data class LoginResponse(
    val username: String,
    val email: String,
    val userId: Int,
    val token: String,
)
