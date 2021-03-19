package org.fairventures.treeo.services

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.fairventures.treeo.getOrAwaitValue
import org.fairventures.treeo.models.LoginDetails
import org.fairventures.treeo.models.RegisterUser
import org.fairventures.treeo.util.BASE_URL
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.mock.BehaviorDelegate
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior

@ExperimentalCoroutinesApi
class RequestManagerTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var retrofit: Retrofit
    private lateinit var mockRetrofit: MockRetrofit
    private lateinit var behavior: NetworkBehavior
    private lateinit var delegate: BehaviorDelegate<ApiService>
    private lateinit var mockedService: FakeApiService
    private lateinit var requestManager: RequestManager

    @Before
    fun setUp() {
        // initialize retrofit
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // initialize mock retrofit
        behavior = NetworkBehavior.create()
        mockRetrofit = MockRetrofit.Builder(retrofit)
            .networkBehavior(behavior)
            .build()

        delegate = mockRetrofit.create(ApiService::class.java)
        mockedService = FakeApiService(delegate)
        requestManager = RequestManager(mockedService)
    }

    @Test
    fun ` test create user endpoint success`() = runBlocking {
        val user = RegisterUser(
            "test-first",
            "test-last",
            "secret",
            "test@gmail.com",
            "country",
            "test-name",
            "0759111222"
        )

        val response = requestManager.createUser(user).getOrAwaitValue()
        assertThat(response.email).isEqualTo(user.email)
    }

    @Test
    fun `test google signup success`() = runBlocking {
        val testToken = "thisisatesttoken"

        val response = requestManager.googleSignUp(testToken).getOrAwaitValue()
        assertThat(response.email).isEqualTo("test@gmail.com")
    }

    @Test
    fun `test facebook signup success`() = runBlocking {
        val accessToken = "thisisatesttoken"

        val response = requestManager.facebookSignUp(accessToken).getOrAwaitValue()
        assertThat(response.email).isEqualTo("test@gmail.com")
    }

    @Test
    fun `test login success`() = runBlocking {
        val loginDetails = LoginDetails("test@gmail.com", "secret")

        val response = requestManager.login(loginDetails).getOrAwaitValue()
        assertThat(response.token).isEqualTo("thisisatesttoken")
    }

    @Test
    fun `test logout success`() = runBlocking {
        val accessToken = "thisisatesttoken"

        val response = requestManager.logout(accessToken).getOrAwaitValue()
        assertThat(response.message).isEqualTo("logout success")
    }
}
