package org.fairventures.treeo.repository

import org.fairventures.treeo.models.*
import org.fairventures.treeo.db.models.Activity

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
            1,
            "thisisatesttoken"
        )
    }

    private fun returnFakeValidatePhoneNumberResponse(): ValidateResponseData {
        return ValidateResponseData(
            errorStatus = "none",
            phoneNumber = "111",
            valid = true
        )
    }

    private fun returnFakeOTP(): String {
        return "OTP Sent"
    }

    private fun returnFakeRegisteredMobileUser(): RegisteredMobileUser {
        return RegisteredMobileUser(
            email = "",
            firstName = "firstname",
            id = 2,
            isActive = true,
            lastName = "lastname"
        )
    }

    private fun returnFakeOTPRegistrationResponse(): ValidateOTPRegistrationResponse {
        return ValidateOTPRegistrationResponse(
            token = "thisisatesttoken"
        )
    }

    override suspend fun login(loginDetails: LoginDetails): LoginResponse {
        return returnFakeLoginResponse(loginDetails)
    }

    private fun returnFakeLogoutResponse(token: String): LogoutResponse {
        return LogoutResponse("user logged out", 200)
    }

    private fun returnFakeSmsLoginResponse(): SmsLoginResponse? {
        return SmsLoginResponse(
            email = "email",
            token = "token",
            userId = 1,
            username = "username"
        )
    }

    private fun returnFakeretrievePlannedActivities(): UserActivities? {
        val activity: List<PlannedActivity> = listOf(PlannedActivity(0, "", false, "", "", null, null))
        return UserActivities(
            firstName = "firstname",
            lastName = "lastname",
            email = "email@gmail.com",
            phoneNumber = "0788934521",
            country = "Uganda",
            username = "username",
            plannedActivites = activity
        )
    }

    override suspend fun logout(token: String): LogoutResponse? {
        return returnFakeLogoutResponse(token)
    }

    override suspend fun postDeviceData(deviceInformation: DeviceInformation, userToken: String) {
        TODO("Not yet implemented")
    }

    override suspend fun validatePhoneNumber(phoneNumber: String): ValidateResponseData? {
        return returnFakeValidatePhoneNumberResponse()
    }

    override suspend fun registerMobileUser(mobileUser: RegisterMobileUser): RegisteredMobileUser? {
        return returnFakeRegisteredMobileUser()
    }

    override suspend fun validateOTPRegistration(validateOTPRegistration: ValidateOTPRegistration): ValidateOTPRegistrationResponse? {
        return returnFakeOTPRegistrationResponse()
    }

    override suspend fun requestOTP(phoneNumber: RequestOTP): String? {
        return returnFakeOTP()
    }

    override suspend fun loginWithOTP(loginWithOTP: LoginWithOTP): SmsLoginResponse? {
        return returnFakeSmsLoginResponse()
    }

    override suspend fun retrievePlannedActivities(userToken: String): UserActivities? {
        return returnFakeretrievePlannedActivities()
    }

}
