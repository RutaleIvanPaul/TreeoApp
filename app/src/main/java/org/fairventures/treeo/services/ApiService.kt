package org.fairventures.treeo.services

import org.fairventures.treeo.models.*
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    @POST("users")
    suspend fun createUser(@Body registerUser: RegisterUser): Response<NewRegisteredUser>

    @POST("auth/google")
    suspend fun googleSignUp(@Body googleAuthToken: GoogleToken): Response<GoogleUser>

    @GET("auth/facebook")
    suspend fun facebookSignUp(@Query("access_token") access_token: String): Response<FacebookUser>

    @POST("auth/login")
    suspend fun login(@Body loginDetails: LoginDetails): Response<BaseResponse<LoginResponse>>

    @GET("auth/logout")
    suspend fun logOut(@Header("Token") token: String): Response<LogoutResponse>

    @POST("device-info")
    suspend fun postDeviceInfo(
        @Body deviceInformation: DeviceInformation,
        @Header("Authorization") token: String
    ): Response<Any>

    @GET("auth/validate-phonenumber/{phoneNumber}")
    suspend fun validatePhoneNumber(
        @Path(value = "phoneNumber", encoded = true)phoneNumber: String
    ): Response<BaseResponse<ValidateResponseData>>

    @POST("auth/request-OTP")
    suspend fun requestOTP(
        @Body phoneNumber: String
    ): Response<PhoneNumberOTPResponse>

    @POST("users/mobile/register")
    suspend fun registerMobileUser(
            @Body mobileUser: RegisterMobileUser
    ):Response<RegisteredMobileUser>

    @POST("/auth/validate-mobile-user")
    suspend fun validateOTPRegistration(
            @Body validateOTPRegistration: ValidateOTPRegistration
    ): Response<ValidateOTPRegistrationResponse>

}
