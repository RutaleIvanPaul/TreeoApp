package org.fairventures.treeo.repository

import org.fairventures.treeo.models.LoginDetails
import org.fairventures.treeo.models.RegisterUser
import org.fairventures.treeo.services.RequestManager
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val requestManager: RequestManager
) {
    suspend fun createUser(registerUser: RegisterUser) = requestManager.createUser(registerUser)

    suspend fun googleSignUp(googleAuthToken: String) = requestManager.googleSignUp(googleAuthToken)

    suspend fun faceBookSignUp(access_token: String) = requestManager.facebookSignUp(access_token)

    suspend fun login(loginDetails: LoginDetails) = requestManager.login(loginDetails)

    suspend fun logout(token: String) = requestManager.logout(token)

}