package org.fairventures.treeo.repository

import org.fairventures.treeo.models.LoginDetails
import org.fairventures.treeo.models.RegisterUser
import org.fairventures.treeo.services.RequestManager
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val requestManager: RequestManager
):IMainRepository {
    override fun createUser(
         registerUser: RegisterUser) = requestManager.createUser(
        registerUser
    )

    override fun googleSignUp(
            googleAuthToken: String
    ) = requestManager.googleSignUP(googleAuthToken)

    override fun faceBookSignUp(
        accessToken: String
    ) = requestManager.facebookSignUp(accessToken)

    override fun login(
        loginDetails: LoginDetails
    ) = requestManager.login(loginDetails)

    override fun logout(
        token: String
    ) = requestManager.logout(token)

}