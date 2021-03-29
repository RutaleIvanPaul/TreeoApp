package org.fairventures.treeo.services

import android.util.Log
import org.fairventures.treeo.models.*
import org.fairventures.treeo.util.errors
import javax.inject.Inject

class RequestManager @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun createUser(registerUser: RegisterUser): NewRegisteredUser? {
        var items: NewRegisteredUser? = null

        val response = apiService.createUser(registerUser)

        if (response.isSuccessful) {
            items = response.body()
        } else {
            Log.d("API ERROR", "API Fetch Error: ${response.message()} ")
            errors.postValue(response.message())
        }
        return items
    }

    suspend fun googleSignUp(googleAuthToken: String): GoogleUser? {
        var items: GoogleUser? = null

        val response = apiService.googleSignUp(GoogleToken(googleAuthToken, "mobile"))

        if (response.isSuccessful) {
            items = response.body()
        } else {
            Log.d("API ERROR", "API Fetch Error: ${response.message()} ")
            errors.postValue(response.message())
        }
        return items
    }

    suspend fun facebookSignUp(access_token: String): FacebookUser? {
        var items: FacebookUser? = null

        val response = apiService.facebookSignUp(access_token)

        if (response.isSuccessful) {
            items = response.body()
            Log.d("Facebook API", response.toString())
        } else {
            Log.d("API ERROR", "API Fetch Error: ${response.message()} ")
            errors.postValue(response.message())
        }
        return items
    }

    suspend fun login(loginDetails: LoginDetails): LoginToken? {
        var items: LoginToken? = null

        val response = apiService.login(loginDetails)

        if (response.isSuccessful) {
            items = response.body()
        } else {
            Log.d("API ERROR", "API Fetch Error: ${response.message()} ")
            errors.postValue(response.message())
        }
        return items
    }

    suspend fun logout(token: String): LogoutResponse? {
        var items: LogoutResponse? = null

        val response = apiService.logOut(token)

        if (response.isSuccessful) {
            items = response.body()
            Log.d("resBody", response.body().toString())
        } else {
            Log.e("resErr", "API Fetch Error: ${response.message()} ")
            errors.postValue(response.message())
        }
        return items
    }

    suspend fun postDeviceData(deviceInformation: DeviceInformation, userToken: String) {
        val response = apiService.postDeviceInfo(deviceInformation, userToken)
        Log.d("devData", deviceInformation.toString())
        if (response.isSuccessful) {
            Log.d("Device Data", "Successfully Added Device Information")
        } else {
            Log.d("API ERROR", "API Fetch Error: ${response.message()} ")
            errors.postValue(response.message())
        }
    }
}
