package org.fairventures.treeo.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.fairventures.treeo.repository.IMainRepository
import org.fairventures.treeo.repository.MainRepository
import org.fairventures.treeo.services.ApiService
import org.fairventures.treeo.services.RequestManager
import org.fairventures.treeo.util.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesDeviceInfoUtils(
        @ApplicationContext context: Context,
        dispatcher: IDispatcherProvider
    ) = DeviceInfoUtils(context, dispatcher)

    @Singleton
    @Provides
    fun providesDispatcherProvider() = DefaultDispatcherProvider() as IDispatcherProvider


    @Singleton
    @Provides
    fun providesRequestManager(apiService: ApiService) =
        RequestManager(apiService)

    @Singleton
    @Provides
    fun providesHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Singleton
    @Provides
    fun providesRetrofit(client: OkHttpClient): ApiService {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesMainRepository(requestManager: RequestManager) =
        MainRepository(requestManager) as IMainRepository

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext applicationContext: Context): SharedPreferences {
        return applicationContext.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
    }

    // Configure sign-in to request the user's ID, email address, and basic
    // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    @Singleton
    @Provides
    fun providesGoogleSignInOptions() =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(GOOGLE_CLIENT_ID)
            .requestEmail()
            .requestProfile()
            .build()
}
