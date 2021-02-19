package com.fairventures.treeo.repository

import com.fairventures.treeo.models.RegisterUser
import com.fairventures.treeo.services.RequestManager
import retrofit2.http.Field
import javax.inject.Inject

class MainRepository @Inject constructor(
    val requestManager: RequestManager
) {
    fun createUser(
         registerUser: RegisterUser) = requestManager.createUser(
        registerUser
    )
}