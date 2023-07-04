package org.fairventures.treeo.services

import android.util.Log
import org.fairventures.treeo.models.*
import org.fairventures.treeo.services.models.ActivityDTO
import org.fairventures.treeo.util.errors
import org.fairventures.treeo.util.getErrorMessageFromJson
import javax.inject.Inject

class RequestManager @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun createUser(registerUser: RegisterUser): NewRegisteredUser? {
        var items: NewRegisteredUser? = null
        try {
            val response = apiService.createUser(registerUser)

            if (response.isSuccessful) {
                items = response.body()
            } else {
                val errorResponse = response.errorBody()!!.charStream().readText()
                errors.postValue(getErrorMessageFromJson(errorResponse))
            }
        } catch (e: Exception) {
            errors.postValue(e.message)
        }
        return items
    }

    suspend fun login(loginDetails: LoginDetails): LoginResponse? {
        var items: LoginResponse? = null
        try {
            val response = apiService.login(loginDetails)

            if (response.isSuccessful) {
                items = response.body()!!.data
            } else {
                val jsonResponse = response.errorBody()!!.charStream().readText()
                errors.postValue(getErrorMessageFromJson(jsonResponse))
            }
        } catch (e: Exception) {
            errors.postValue(e.message)
        }
        return items
    }

    suspend fun logout(token: String): LogoutResponse? {
        var items: LogoutResponse? = null
        try {
            val response = apiService.logOut(token)

            if (response.isSuccessful) {
                items = response.body()
                Log.d("resBody", response.body().toString())
            } else {
                val jsonResponse = response.errorBody()!!.charStream().readText()
                errors.postValue(getErrorMessageFromJson(jsonResponse))
            }
        } catch (e: Exception) {
            errors.postValue(e.message)
        }
        return items
    }

    suspend fun postDeviceData(deviceInformation: DeviceInformation, userToken: String) {
        Log.d("devData", deviceInformation.toString())
        try {
            val response = apiService.postDeviceInfo(deviceInformation, userToken)

            if (response.isSuccessful) {
                Log.d("Device Data", "Successfully Added Device Information")
            } else {
                val jsonResponse = response.errorBody()!!.charStream().readText()
                errors.postValue(getErrorMessageFromJson(jsonResponse))
            }
        } catch (e: Exception) {
            errors.postValue(e.message)
        }
    }

    suspend fun validatePhoneNumber(phoneNumber: String): ValidateResponseData? {
        var items: ValidateResponseData? = null
        try {
            val response = apiService.validatePhoneNumber(phoneNumber)

            if (response.isSuccessful) {
                items = response.body()?.data
            } else {
                if (response.code() == 404) {
                    items = ValidateResponseData(
                        errorStatus = "",
                        phoneNumber = "",
                        valid = false
                    )
                }
                val jsonResponse = response.errorBody()!!.charStream().readText()
                errors.postValue(getErrorMessageFromJson(jsonResponse))
            }
        } catch (e: Exception) {
            errors.postValue(e.message)
        }
        return items
    }

    suspend fun requestOTP(phoneNumber: RequestOTP): String? {
        var items: String? = null
        try {
            val response = apiService.requestOTP(phoneNumber)

            if (response.isSuccessful) {
                items = response.body()?.message
            } else {
                val jsonResponse = response.errorBody()!!.charStream().readText()
                errors.postValue(getErrorMessageFromJson(jsonResponse))
            }
        } catch (e: Exception) {
            errors.postValue(e.message)
        }
        return items
    }


    suspend fun registerMobileUser(mobileUser: RegisterMobileUser): RegisteredMobileUser? {
        var items: RegisteredMobileUser? = null
        try {
            val response = apiService.registerMobileUser(mobileUser)

            if (response.isSuccessful) {
                items = response.body()
            } else {
                val jsonResponse = response.errorBody()!!.charStream().readText()
                errors.postValue(getErrorMessageFromJson(jsonResponse))
            }
        } catch (e: Exception) {
            errors.postValue(e.message)
        }
        return items
    }

    suspend fun validateOTPRegistration(
        validateOTPRegistration: ValidateOTPRegistration
    ): ValidateOTPRegistrationResponse? {
        var items: ValidateOTPRegistrationResponse? = null
        try {
            val response = apiService.validateOTPRegistration(validateOTPRegistration)

            if (response.isSuccessful) {
                items = response.body()?.data
            } else {
                val jsonResponse = response.errorBody()!!.charStream().readText()
                errors.postValue(getErrorMessageFromJson(jsonResponse))
            }
        } catch (e: Exception) {
            errors.postValue(e.message)
        }
        return items
    }

    suspend fun loginWithOTP(loginWithOTP: LoginWithOTP): SmsLoginResponse? {
        var items: SmsLoginResponse? = null
        try {
            val response = apiService.loginWitOTP(loginWithOTP)

            if (response.isSuccessful) {
                items = response.body()?.data
            } else {
                val jsonResponse = response.errorBody()!!.charStream().readText()
                errors.postValue(getErrorMessageFromJson(jsonResponse))
            }
        } catch (e: Exception) {
            errors.postValue(e.message)
        }
        return items
    }

    suspend fun retrievePlannedActivities(userToken: String): List<ActivityDTO> {
        var activities = listOf<ActivityDTO>()
        try {
            val response = apiService.retrievePlannedActivities(userToken)

            if (response.isSuccessful) {
                activities = response.body()!!.plannedActivities
                Log.d("ActivitiesRM", activities.toString())
            } else {
                if (response.code() == 404) {
                    Log.d("Message", "Unauthorized")
                }
                val jsonResponse = response.errorBody()!!.charStream().readText()
                errors.postValue(getErrorMessageFromJson(jsonResponse))
            }
        } catch (e: Exception) {
            errors.postValue(e.message)
        }
        return activities
    }

}
