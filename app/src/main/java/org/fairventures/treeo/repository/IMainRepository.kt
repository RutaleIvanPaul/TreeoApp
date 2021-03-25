package org.fairventures.treeo.repository

import androidx.lifecycle.MutableLiveData
import org.fairventures.treeo.models.*

interface IMainRepository {
    suspend fun createUser(registerUser: RegisterUser): NewRegisteredUser?
    suspend fun googleSignUp(googleAuthToken: String): GoogleUser?
    suspend fun faceBookSignUp(accessToken: String): FacebookUser?
    suspend fun login(loginDetails: LoginDetails): LoginToken?
    suspend fun logout(token: String): LogoutResponse?
    suspend fun postDeviceData(deviceInformation: DeviceInformation, userToken: String)
}
