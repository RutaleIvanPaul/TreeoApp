package org.fairventures.treeo.repository

import org.fairventures.treeo.models.*

interface IMainRepository {
    suspend fun createUser(registerUser: RegisterUser): NewRegisteredUser?
    suspend fun googleSignUp(googleAuthToken: String): GoogleUser?
    suspend fun faceBookSignUp(accessToken: String): FacebookUser?
    suspend fun login(loginDetails: LoginDetails): LoginResponse?
    suspend fun logout(token: String): LogoutResponse?
    suspend fun postDeviceData(deviceInformation: DeviceInformation, userToken: String)
    suspend fun validatePhoneNumber(phoneNumber: String): ValidateResponseData?
    suspend fun requestOTP(phoneNumber: String): PhoneNumberOTPResponse?
    suspend fun registerMobileUser(mobileUser: RegisterMobileUser): RegisteredMobileUser?
    suspend fun validateOTPRegistration(
            validateOTPRegistration: ValidateOTPRegistration
    ): ValidateOTPRegistrationResponse?
}
