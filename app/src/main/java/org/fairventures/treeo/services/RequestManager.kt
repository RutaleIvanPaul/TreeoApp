package org.fairventures.treeo.services

import android.util.Log
import androidx.lifecycle.MutableLiveData
import org.fairventures.treeo.models.*
import org.fairventures.treeo.util.errors
import javax.inject.Inject

class RequestManager @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun createUser(registerUser: RegisterUser): MutableLiveData<NewRegisteredUser> {
        val items = MutableLiveData<NewRegisteredUser>()

        val response = apiService.createUser(registerUser)

        if (response.isSuccessful) {
            items.postValue(response.body())
        } else {
            Log.d("API ERROR", "API Fetch Error: ${response.message()} ")
            errors.postValue(response.message())
        }
        return items
    }

    suspend fun googleSignUp(googleAuthToken: String): MutableLiveData<GoogleUser> {
        val items = MutableLiveData<GoogleUser>()

        val response = apiService.googleSignUp(GoogleToken(googleAuthToken, "mobile"))

        if (response.isSuccessful) {
            items.postValue(response.body())
        } else {
            Log.d("API ERROR", "API Fetch Error: ${response.message()} ")
            errors.postValue(response.message())
        }
        return items
    }

    suspend fun facebookSignUp(access_token: String): MutableLiveData<FacebookUser> {
        val items = MutableLiveData<FacebookUser>()

        val response = apiService.facebookSignUp(access_token)

        if (response.isSuccessful) {
            items.postValue(response.body())
            Log.d("Facebook API", response.toString())
        } else {
            Log.d("API ERROR", "API Fetch Error: ${response.message()} ")
            errors.postValue(response.message())
        }
        return items
    }

    suspend fun login(loginDetails: LoginDetails): MutableLiveData<LoginToken> {
        val items = MutableLiveData<LoginToken>()

        val response = apiService.login(loginDetails)

        if (response.isSuccessful) {
            items.postValue(response.body())
        } else {
            Log.d("API ERROR", "API Fetch Error: ${response.message()} ")
            errors.postValue(response.message())
        }
        return items
    }

    suspend fun logout(token: String): MutableLiveData<LogoutResponse> {
        val items = MutableLiveData<LogoutResponse>()

        val response = apiService.logOut(token)

        if (response.isSuccessful) {
            items.postValue(response.body())
        } else {
            Log.d("API ERROR", "API Fetch Error: ${response.message()} ")
            errors.postValue(response.message())
        }
        return items
    }
}
