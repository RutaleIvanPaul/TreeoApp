package com.fairventures.treeo.services

import com.fairventures.treeo.models.FarmerFromApi
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {

    @GET("farmers")
    fun returnFarmers():
            Observable<FarmerFromApi>

    companion object{
        fun create(): ApiService{
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(
                    RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                    GsonConverterFactory.create())
                .baseUrl("https://fakeapi20201120181506.azurewebsites.net/")
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}