package org.fairventures.treeo.services

import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.fairventures.treeo.models.*
import org.fairventures.treeo.util.BASE_URL
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface ApiService {

    @POST("users")
    fun createUser(
        @Body registerUser: RegisterUser
    ): Observable<NewRegisteredUser>

    @POST("auth/google")
    fun googleSignUp(
        @Body googleAuthToken: GoogleToken
    ): Observable<GoogleUser>

    @POST("auth/jwt")
    fun login(
        @Body loginDetails: LoginDetails
    ): Observable<LoginToken>

    @GET("auth/facebook")
    fun facebookSignUp(
        @Query("access_token") access_token: String
    ): Observable<FacebookUser>

    @GET("/auth/logout")
    fun logOut(
        @Header("Token") token: String
    ): Observable<LogoutResponse>

    companion object{
        fun create(): ApiService {


            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()


            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(
                    RxJava2CallAdapterFactory.create()
                )
                .addConverterFactory(
                    GsonConverterFactory.create()
                )
                .baseUrl(BASE_URL)
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}