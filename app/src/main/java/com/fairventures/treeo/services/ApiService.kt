package com.fairventures.treeo.services

import com.fairventures.treeo.models.NewRegisteredUser
import com.fairventures.treeo.models.RegisterUser
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {

    @POST("users")
    fun createUser(
        @Body registerUser: RegisterUser
    ): Observable<NewRegisteredUser>

    companion object{
        fun create(): ApiService{
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(
                    RxJava2CallAdapterFactory.create()
                )
                .addConverterFactory(
                    GsonConverterFactory.create()
                )
                .baseUrl("http://3.64.149.86:9001/")
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}