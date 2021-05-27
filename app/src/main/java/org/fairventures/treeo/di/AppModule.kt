package org.fairventures.treeo.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.fairventures.treeo.db.TreeoDatabase
import org.fairventures.treeo.db.dao.ActivityDao
import org.fairventures.treeo.db.dao.LandSurveyDao
import org.fairventures.treeo.db.models.mappers.ActivityDtoToEntityMapper
import org.fairventures.treeo.models.mappers.QuestionnaireWithPagesMapper
import org.fairventures.treeo.repositories.DBMainRepository
import org.fairventures.treeo.repositories.IMainRepository
import org.fairventures.treeo.repositories.MainRepository
import org.fairventures.treeo.services.ApiService
import org.fairventures.treeo.services.RequestManager
import org.fairventures.treeo.util.*
import org.fairventures.treeo.util.mappers.DtoEntityMapper
import org.fairventures.treeo.util.mappers.ModelDtoMapper
import org.fairventures.treeo.util.mappers.ModelEntityMapper
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
    fun providesNetworkInterceptor(@ApplicationContext context: Context) =
        NetworkConnectionInterceptor(context)

    @Singleton
    @Provides
    fun providesHttpClient(networkInterceptor: NetworkConnectionInterceptor): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(networkInterceptor)
            .addInterceptor(interceptor)
            .build()
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

    @Singleton
    @Provides
    fun providesTreeoDatabase(@ApplicationContext applicationContext: Context): TreeoDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TreeoDatabase::class.java,
            TREEO_DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun providesActivityDao(database: TreeoDatabase) =
        database.getActivityDao()

    @Singleton
    @Provides
    fun providesLandSurveyDao(database: TreeoDatabase) =
        database.getLandSurveyDao()

//    @Singleton
//    @Provides
//    fun providesQuestionnaireAnswerDao(database: TreeoDatabase) =
//        database.getQuestionnaireAnswerDao()

    @Singleton
    @Provides
    fun providesDBMainRepository(activityDao: ActivityDao,landSurveyDao: LandSurveyDao, mapper: ActivityDtoToEntityMapper) =
        DBMainRepository(activityDao,landSurveyDao, mapper)

    @Singleton
    @Provides
    fun providesActivityDtoToEntityMapper() = ActivityDtoToEntityMapper()

    @Singleton
    @Provides
    fun providesQuestionnaireWithPagesMapper() = QuestionnaireWithPagesMapper()

    @Singleton
    @Provides
    fun providesDtoEntityMapper() = DtoEntityMapper()

    @Singleton
    @Provides
    fun providesModelDtoMapper() = ModelDtoMapper()

    @Singleton
    @Provides
    fun providesModelEntityMapper() = ModelEntityMapper()

}
