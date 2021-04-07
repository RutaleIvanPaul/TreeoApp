package org.fairventures.treeo.services

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.fairventures.treeo.getOrAwaitValue
import org.fairventures.treeo.models.*
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
    fun ` test create user success`() = runBlocking {
        val user = RegisterUser(
            "test-first",
            "test-last",
            "secret",
            "test@gmail.com",
            "country",
            "test-name",
            "999"
        )

        val response = requestManager.createUser(user)
        assertThat(response?.email).isEqualTo(user.email)
    }

    @Test
    fun `test google signup success`() = runBlocking {
        val testToken = "thisisatesttoken"

        val response = requestManager.googleSignUp(testToken)
        assertThat(response?.email).isEqualTo("test@gmail.com")
    }

    @Test
    fun `test facebook signup success`() = runBlocking {
        val accessToken = "thisisatesttoken"

        val response = requestManager.facebookSignUp(accessToken)
        assertThat(response?.email).isEqualTo("test@gmail.com")
    }

    @Test
    fun `test login success`() = runBlocking {
        val loginDetails = LoginDetails("test@gmail.com", "secret")

        val response = requestManager.login(loginDetails)
        assertThat(response?.token).isEqualTo("thisisatesttoken")
    }

    @Test
    fun `test logout success`() = runBlocking {
        val accessToken = "thisisatesttoken"

        val response = requestManager.logout(accessToken)
        assertThat(response?.message).isEqualTo("logout success")
    }

    @Test
    fun `test validatePhoneNumber failure`() = runBlocking {
        val response = requestManager.validatePhoneNumber("123")
        assertThat(response?.valid).isFalse()
    }

    @Test
    fun `test registerMobileUser success`() = runBlocking {
        val mobileUser = RegisterMobileUser(
                firstName = "firstname",
                lastName = "lastname"  ,
                country = "Uganda",
                password = "password",
                phoneNumber = "123",
                username = "username"
        )
        val response =  requestManager.registerMobileUser(mobileUser)
        assertThat(response?.firstName).isEqualTo(mobileUser.firstName)
    }

    @Test
    fun `test validateOTPRegistration success`() = runBlocking {
        val validateOTPRegistration = ValidateOTPRegistration(
                code =  "123",
                phoneNumber = "123"
        )
        val response = requestManager.validateOTPRegistration(validateOTPRegistration)
        assertThat(response?.message).isEqualTo("User Active")
    }

}
