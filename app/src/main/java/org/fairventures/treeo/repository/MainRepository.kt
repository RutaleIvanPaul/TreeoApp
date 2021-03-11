package org.fairventures.treeo.repository

import org.fairventures.treeo.models.LoginDetails
import org.fairventures.treeo.models.RegisterUser
import org.fairventures.treeo.services.RequestManager
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val requestManager: RequestManager
) : IMainRepository {
    override suspend fun createUser(registerUser: RegisterUser) =
        requestManager.createUser(registerUser)

    override suspend fun googleSignUp(googleAuthToken: String) =
        requestManager.googleSignUp(googleAuthToken)

    override suspend fun faceBookSignUp(accessToken: String) =
        requestManager.facebookSignUp(accessToken)

    override suspend fun login(loginDetails: LoginDetails) = requestManager.login(loginDetails)

    override suspend fun logout(token: String) = requestManager.logout(token)
}
