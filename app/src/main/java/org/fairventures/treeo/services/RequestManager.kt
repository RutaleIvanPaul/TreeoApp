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
            Log.d("fbLog", response.toString())
        } else {
            Log.d("API ERROR", "API Fetch Error: ${response.message()} ")
            errors.postValue(response.message())
        }
        return items
    }

    suspend fun login(loginDetails: LoginDetails): LoginResponse? {
        var items: LoginResponse? = null

        val response = apiService.login(loginDetails)

        if (response.isSuccessful) {
            items = response.body()!!.data
            Log.d("logRes", response.body()!!.data.toString())
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
        Log.d("devData", deviceInformation.toString())

        val response = apiService.postDeviceInfo(deviceInformation, userToken)

        if (response.isSuccessful) {
            Log.d("Device Data", "Successfully Added Device Information")
        } else {
            Log.d("API ERROR", "API Fetch Error: ${response.message()} ")
            errors.postValue(response.message())
        }
    }

    suspend fun validatePhoneNumber(phoneNumber: String): ValidateResponseData? {
        var items: ValidateResponseData? = null

        val response = apiService.validatePhoneNumber(phoneNumber)

        if (response.isSuccessful) {
            items = response.body()?.data
        }
        else {
            if (response.code() == 404){
                items = ValidateResponseData(
                    errorStatus = "",
                    phoneNumber = "",
                    valid = false
                )
            }
            Log.d("API ERROR", "API Fetch Error: ${response.message()} ")
            errors.postValue(response.message())
        }

        return items
    }

    suspend fun requestOTP(phoneNumber: String): PhoneNumberOTPResponse? {
        var items: PhoneNumberOTPResponse? = null

        val response = apiService.requestOTP(phoneNumber)

        if (response.isSuccessful) {
            items = response.body()
        } else {
            Log.d("API ERROR", "API Fetch Error: ${response.message()} ")
            errors.postValue(response.message())
        }

        return items
    }


    suspend fun registerMobileUser(mobileUser: RegisterMobileUser):RegisteredMobileUser?{
        var items: RegisteredMobileUser? = null

        val response = apiService.registerMobileUser(mobileUser)

        if (response.isSuccessful) {
            items = response.body()
        } else {
            Log.d("API ERROR", "API Fetch Error: ${response.message()} ")
            errors.postValue(response.message())
        }

        return items
    }

    suspend fun validateOTPRegistration(
            validateOTPRegistration: ValidateOTPRegistration
    ):ValidateOTPRegistrationResponse?{
        var items: ValidateOTPRegistrationResponse? = null

        val response =
                apiService.validateOTPRegistration(validateOTPRegistration)

        if (response.isSuccessful) {
            items = response.body()
        } else {
            Log.d("API ERROR", "API Fetch Error: ${response.message()} ")
            errors.postValue(response.message())
        }

        return items
    }

}
