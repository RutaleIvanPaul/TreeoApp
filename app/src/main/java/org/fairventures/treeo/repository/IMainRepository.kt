package org.fairventures.treeo.repository

import androidx.lifecycle.MutableLiveData
import org.fairventures.treeo.models.*

interface IMainRepository {
    suspend fun createUser(registerUser: RegisterUser): MutableLiveData<NewRegisteredUser>
    suspend fun googleSignUp(googleAuthToken: String): MutableLiveData<GoogleUser>
    suspend fun faceBookSignUp(accessToken: String): MutableLiveData<FacebookUser>
    suspend fun login(loginDetails: LoginDetails): MutableLiveData<LoginToken>
    suspend fun logout(token: String): MutableLiveData<LogoutResponse>
    suspend fun postDeviceData(deviceInformation: DeviceInformation, userToken: String)
}
