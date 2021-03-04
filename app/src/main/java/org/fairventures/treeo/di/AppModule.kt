package org.fairventures.treeo.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import org.fairventures.treeo.services.RequestManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import org.fairventures.treeo.util.GOOGLE_CLIENT_ID
import org.fairventures.treeo.util.SHARED_PREFERENCES_NAME
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesRequestManager()
            = RequestManager()


    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext applicationContext: Context ): SharedPreferences {
        return applicationContext.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
    }

    // Configure sign-in to request the user's ID, email address, and basic
    // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    @Singleton
    @Provides
    fun providesGoogleSignInOptions()
    = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(GOOGLE_CLIENT_ID)
        .requestEmail()
        .requestProfile()
        .build()

}