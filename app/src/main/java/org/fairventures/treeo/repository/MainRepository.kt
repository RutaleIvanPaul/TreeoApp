package org.fairventures.treeo.repository

import org.fairventures.treeo.models.RegisterUser
import org.fairventures.treeo.services.RequestManager
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