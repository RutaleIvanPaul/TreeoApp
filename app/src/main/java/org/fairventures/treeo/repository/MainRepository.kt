package org.fairventures.treeo.repository

import org.fairventures.treeo.models.LoginDetails
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

    fun googleSignUp(
            googleAuthToken: String
    ) = requestManager.googleSignUP(googleAuthToken)

    fun faceBookSignUp(
        access_token: String
    ) = requestManager.facebookSignUp(access_token)

    fun login(
        loginDetails: LoginDetails
    ) = requestManager.login(loginDetails)

    fun logout(
        token: String
    ) = requestManager.logout(token)

}