package org.fairventures.treeo.repository

import androidx.lifecycle.MutableLiveData
import org.fairventures.treeo.models.*

class FakeMainRepository : IMainRepository {
    private fun returnFakeUser(registerUser: RegisterUser): NewRegisteredUser {
        val user = NewRegisteredUser(
            registerUser.email,
            registerUser.firstName,
            false,
            registerUser.lastName
        )
        return user
    }

    override suspend fun createUser(registerUser: RegisterUser): NewRegisteredUser? {
        return returnFakeUser(registerUser)
    }

    private fun returnFakeGoogleUser(googleAuthToken: String): GoogleUser {
        val googleUser = GoogleUser(
                "username",
            "googleuser@gmail.com",
                googleAuthToken,
            200
        )
        return googleUser
    }

    override suspend fun googleSignUp(googleAuthToken: String): GoogleUser? {
        return returnFakeGoogleUser(googleAuthToken)
    }

    private fun returnFakeFacebookUser(accessToken: String):FacebookUser {
        val facebookUser = FacebookUser(
            "face@gmail.com",
            "firstName",
            "lastName",
            0,
            accessToken,
            "username"
        )
        return facebookUser
    }

    override suspend fun faceBookSignUp(accessToken: String): FacebookUser? {
        return returnFakeFacebookUser(accessToken)
    }

    private fun returnFakeLoginToken(loginDetails: LoginDetails): LoginToken {
        val loginToken = LoginToken(
                "thisisatestusername",
                "email","thisisanauthtoken",200)
        return loginToken
    }

    override suspend fun login(loginDetails: LoginDetails): LoginToken {
        return returnFakeLoginToken(loginDetails)
    }

    private fun returnFakeLogoutResponse(token: String): LogoutResponse {
        val logoutResponse = LogoutResponse("user logged out", 200)
        return logoutResponse
    }

    override suspend fun logout(token: String): LogoutResponse? {
        return returnFakeLogoutResponse(token)
    }

    override suspend fun postDeviceData(deviceInformation: DeviceInformation, userToken: String) {
        TODO("Not yet implemented")
    }
}
