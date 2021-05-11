package org.fairventures.treeo.repository

import org.fairventures.treeo.models.*
import org.fairventures.treeo.services.RequestManager
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val requestManager: RequestManager
) : IMainRepository {
    override suspend fun createUser(registerUser: RegisterUser) =
        requestManager.createUser(registerUser)

    override suspend fun login(loginDetails: LoginDetails) =
        requestManager.login(loginDetails)

    override suspend fun logout(token: String) =
        requestManager.logout(token)

    override suspend fun postDeviceData(deviceInformation: DeviceInformation, userToken: String) =
        requestManager.postDeviceData(deviceInformation, userToken)

    override suspend fun validatePhoneNumber(phoneNumber: String) =
        requestManager.validatePhoneNumber(phoneNumber)

    override suspend fun requestOTP(phoneNumber: RequestOTP): String? =
        requestManager.requestOTP(phoneNumber)

    override suspend fun registerMobileUser(mobileUser: RegisterMobileUser) =
        requestManager.registerMobileUser(mobileUser)

    override suspend fun validateOTPRegistration(validateOTPRegistration: ValidateOTPRegistration) =
        requestManager.validateOTPRegistration(
            validateOTPRegistration
        )

    override suspend fun loginWithOTP(loginWithOTP: LoginWithOTP): SmsLoginResponse? =
        requestManager.loginWithOTP(loginWithOTP)

    override suspend fun retrievePlannedActivities(userToken: String): UserActivities? =
        requestManager.retrievePlannedActivities(userToken)
}
