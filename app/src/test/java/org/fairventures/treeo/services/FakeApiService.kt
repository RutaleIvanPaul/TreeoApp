package org.fairventures.treeo.services

import org.fairventures.treeo.models.*
import retrofit2.Response
import retrofit2.mock.BehaviorDelegate

class FakeApiService(private val delegate: BehaviorDelegate<ApiService>) : ApiService {

    override suspend fun createUser(registerUser: RegisterUser): Response<NewRegisteredUser> {
        val user = NewRegisteredUser(
            registerUser.email,
            registerUser.firstName,
            false,
            registerUser.lastName
        )
        return delegate.returningResponse(user).createUser(registerUser)
    }

    override suspend fun googleSignUp(googleAuthToken: GoogleToken): Response<GoogleUser> {
        val googleUser = GoogleUser(
            "tester",
            "test@gmail.com",
            "thisisatesttoken",
            200
        )
        return delegate.returningResponse(googleUser).googleSignUp(googleAuthToken)
    }

    override suspend fun facebookSignUp(access_token: String): Response<FacebookUser> {
        val facebookUser = FacebookUser(
            "test@gmail.com",
            "firstname",
            "lastname",
            200,
            "thisisatesttoken",
            "username"
        )
        return delegate.returningResponse(facebookUser).facebookSignUp(access_token)
    }

    override suspend fun login(loginDetails: LoginDetails): Response<BaseResponse<LoginResponse>> {
        val loginResponse = LoginResponse(
            "username",
            "test@gmail.com",
            1,
            "thisisatesttoken"
        )
        val baseResponse = BaseResponse(loginResponse, "success")
        return delegate.returningResponse(baseResponse).login(loginDetails)
    }

    override suspend fun logOut(token: String): Response<LogoutResponse> {
        val logoutResponse = LogoutResponse("logout success", 200)
        return delegate.returningResponse(logoutResponse).logOut(token)
    }

    override suspend fun postDeviceInfo(
        deviceInformation: DeviceInformation,
        token: String
    ): Response<Any> {
        TODO("Not yet implemented")
    }

    override suspend fun validatePhoneNumber(phoneNumber: String): Response<BaseResponse<ValidateResponseData>> {
        val validateResponseData = ValidateResponseData(
            errorStatus = "not found",
            phoneNumber = "123",
            valid = false
        )

        val baseResponse = BaseResponse(
            validateResponseData,
            "not found"
        )

        return delegate.returningResponse(baseResponse).validatePhoneNumber(phoneNumber)
    }

    override suspend fun requestOTP(phoneNumber: String): Response<PhoneNumberOTPResponse> {
        val phoneNumberOTPResponse = PhoneNumberOTPResponse(
            phoneNumber = "123",
            status = "true"
        )
        return delegate.returningResponse(phoneNumberOTPResponse).requestOTP(phoneNumber)
    }

    override suspend fun registerMobileUser(mobileUser: RegisterMobileUser): Response<RegisteredMobileUser> {
        val registeredMobileUser = RegisteredMobileUser(
                email = "",
                firstName = "firstname",
                id = 2,
                isActive = true,
                lastName = "lastname"
        )

        return delegate.returningResponse(registeredMobileUser).registerMobileUser(mobileUser)
    }

    override suspend fun validateOTPRegistration(validateOTPRegistration: ValidateOTPRegistration): Response<ValidateOTPRegistrationResponse> {
        val validateOTPRegistrationResponse = ValidateOTPRegistrationResponse(
                data = "",
                message = "User Active"
        )

        return delegate.returningResponse(validateOTPRegistrationResponse).validateOTPRegistration(validateOTPRegistration)
    }
}
