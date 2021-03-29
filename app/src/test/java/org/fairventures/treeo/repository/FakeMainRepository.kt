package org.fairventures.treeo.repository

import org.fairventures.treeo.models.*

class FakeMainRepository : IMainRepository {
    private fun returnFakeUser(registerUser: RegisterUser): NewRegisteredUser {
        return NewRegisteredUser(
            registerUser.email,
            registerUser.firstName,
            false,
            registerUser.lastName
        )
    }

    override suspend fun createUser(registerUser: RegisterUser): NewRegisteredUser? {
        return returnFakeUser(registerUser)
    }

    private fun returnFakeGoogleUser(googleAuthToken: String): GoogleUser {
        return GoogleUser(
            "username",
            "googleuser@gmail.com",
            googleAuthToken,
            200
        )
    }

    override suspend fun googleSignUp(googleAuthToken: String): GoogleUser? {
        return returnFakeGoogleUser(googleAuthToken)
    }

    private fun returnFakeFacebookUser(accessToken: String): FacebookUser {
        return FacebookUser(
            "face@gmail.com",
            "firstName",
            "lastName",
            0,
            accessToken,
            "username"
        )
    }

    override suspend fun faceBookSignUp(accessToken: String): FacebookUser? {
        return returnFakeFacebookUser(accessToken)
    }

    private fun returnFakeLoginResponse(loginDetails: LoginDetails): LoginResponse {
        return LoginResponse(
            "username",
            loginDetails.email,
            "thisisanauthtoken",
            200
        )
    }

    override suspend fun login(loginDetails: LoginDetails): LoginResponse {
        return returnFakeLoginResponse(loginDetails)
    }

    private fun returnFakeLogoutResponse(token: String): LogoutResponse {
        return LogoutResponse("user logged out", 200)
    }

    override suspend fun logout(token: String): LogoutResponse? {
        return returnFakeLogoutResponse(token)
    }

    override suspend fun postDeviceData(deviceInformation: DeviceInformation, userToken: String) {
        TODO("Not yet implemented")
    }
}
