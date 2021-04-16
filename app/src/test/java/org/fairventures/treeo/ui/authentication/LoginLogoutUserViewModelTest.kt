package org.fairventures.treeo.ui.authentication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.fairventures.treeo.MainCoroutineRule
import org.fairventures.treeo.models.LoginDetails
import org.fairventures.treeo.models.LoginWithOTP
import org.fairventures.treeo.models.LogoutResponse
import org.fairventures.treeo.models.RequestOTP
import org.fairventures.treeo.repository.FakeMainRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginLogoutUserViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: LoginLogoutUserViewModel

    @Before
    fun setUp() {
        viewModel = LoginLogoutUserViewModel(
            FakeMainRepository(),
            mainCoroutineRule.testDispatcherProvider
        )
    }

    @Test
    fun `test login`() = mainCoroutineRule.testDispatcher.runBlockingTest {
        val loginDetails = LoginDetails("test@gmail.com", "secret")
        val expectedToken = "thisisatesttoken"
        viewModel.login(loginDetails.email, loginDetails.password)
        assertThat(viewModel.loginToken.value?.token).isEqualTo(expectedToken)
    }

    @Test
    fun `test logout`() = mainCoroutineRule.testDispatcher.runBlockingTest {
        val expectedResponse = LogoutResponse("user logged out", 200)
        viewModel.logout("thisisanauthtoken")
        assertThat(viewModel.logoutResponse.value?.message).isEqualTo(expectedResponse.message)
    }

    @Test
    fun `test requestOTP`() = mainCoroutineRule.testDispatcher.runBlockingTest {
        val requestOTP = RequestOTP(
            "+123"
        )
        viewModel.requestOTP(requestOTP)
        assertThat(viewModel.phoneNumberOTPResponse.value).isEqualTo("OTP Sent")
    }

    @Test
    fun `test loginWithOTP`() = mainCoroutineRule.testDispatcher.runBlockingTest {
        val loginWithOTP = LoginWithOTP(
            "+123",
            "123"
        )
        viewModel.loginWithOTP(loginWithOTP)
        assertThat(viewModel.smsLoginResponse.value?.token).isEqualTo("token")
    }

}
