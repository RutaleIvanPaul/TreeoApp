package org.fairventures.treeo.repositories

import org.fairventures.treeo.models.*
import org.fairventures.treeo.services.models.ActivityDTO

interface IMainRepository {
    suspend fun createUser(registerUser: RegisterUser): NewRegisteredUser?
    suspend fun login(loginDetails: LoginDetails): LoginResponse?
    suspend fun logout(token: String): LogoutResponse?
    suspend fun postDeviceData(deviceInformation: DeviceInformation, userToken: String)
    suspend fun validatePhoneNumber(phoneNumber: String): ValidateResponseData?
    suspend fun requestOTP(phoneNumber: RequestOTP): String?
    suspend fun registerMobileUser(mobileUser: RegisterMobileUser): RegisteredMobileUser?
    suspend fun validateOTPRegistration(
        validateOTPRegistration: ValidateOTPRegistration
    ): ValidateOTPRegistrationResponse?

    suspend fun loginWithOTP(loginWithOTP: LoginWithOTP): SmsLoginResponse?
    suspend fun retrievePlannedActivities(userToken: String): List<ActivityDTO>?
}
