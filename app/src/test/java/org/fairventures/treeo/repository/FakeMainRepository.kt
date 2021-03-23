package org.fairventures.treeo.repository

import androidx.lifecycle.MutableLiveData
import org.fairventures.treeo.models.*

class FakeMainRepository : IMainRepository {
    private fun returnFakeUser(registerUser: RegisterUser): MutableLiveData<NewRegisteredUser> {
        val user = NewRegisteredUser(
            registerUser.email,
            registerUser.firstName,
            false,
            registerUser.lastName
        )
        val userLiveData = MutableLiveData<NewRegisteredUser>()
        userLiveData.value = user
        return userLiveData
    }

    override suspend fun createUser(registerUser: RegisterUser): MutableLiveData<NewRegisteredUser> {
        return returnFakeUser(registerUser)
    }

    private fun returnFakeGoogleUser(googleAuthToken: String): MutableLiveData<GoogleUser> {
        val googleUser = GoogleUser(
            "googleuser@gmail.com",
            0,
            googleAuthToken,
            "username"
        )
        val userLiveData = MutableLiveData<GoogleUser>()
        userLiveData.value = googleUser
        return userLiveData
    }

    override suspend fun googleSignUp(googleAuthToken: String): MutableLiveData<GoogleUser> {
        return returnFakeGoogleUser(googleAuthToken)
    }

    private fun returnFakeFacebookUser(accessToken: String): MutableLiveData<FacebookUser> {
        val facebookUser = FacebookUser(
            "face@gmail.com",
            "firstName",
            "lastName",
            0,
            accessToken,
            "username"
        )
        val userLiveData = MutableLiveData<FacebookUser>()
        userLiveData.value = facebookUser
        return userLiveData
    }

    override suspend fun faceBookSignUp(accessToken: String): MutableLiveData<FacebookUser> {
        return returnFakeFacebookUser(accessToken)
    }

    private fun returnFakeLoginToken(loginDetails: LoginDetails): MutableLiveData<LoginToken> {
        val loginToken = LoginToken(
                "thisisatestusername",
                "email","thisisanauthtoken",200)
        val tokenLiveData = MutableLiveData<LoginToken>()
        tokenLiveData.value = loginToken
        return tokenLiveData
    }

    override suspend fun login(loginDetails: LoginDetails): MutableLiveData<LoginToken> {
        return returnFakeLoginToken(loginDetails)
    }

    private fun returnFakeLogoutResponse(token: String): MutableLiveData<LogoutResponse> {
        val logoutResponse = LogoutResponse("user logged out", 200)
        val logoutLiveData = MutableLiveData<LogoutResponse>()
        logoutLiveData.value = logoutResponse
        return logoutLiveData
    }

    override suspend fun logout(token: String): MutableLiveData<LogoutResponse> {
        return returnFakeLogoutResponse(token)
    }

    override suspend fun postDeviceData(deviceInformation: DeviceInformation, userToken: String) {
        TODO("Not yet implemented")
    }
}
