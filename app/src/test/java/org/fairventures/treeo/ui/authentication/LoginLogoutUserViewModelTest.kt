package org.fairventures.treeo.ui.authentication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import org.fairventures.treeo.getOrAwaitValue
import org.fairventures.treeo.models.LoginDetails
import org.fairventures.treeo.models.LogoutResponse
import org.fairventures.treeo.repository.FakeMainRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginLogoutUserViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: LoginLogoutUserViewModel

    @Before
    fun setUp() {
        viewModel = LoginLogoutUserViewModel(FakeMainRepository())
    }

    @Test
    fun `test login`() {
        val loginDetails = LoginDetails("test@gmail.com", "secret")
        val expectedToken = "thisisanauthtoken"
        val value = viewModel.login(loginDetails.email, loginDetails.password).getOrAwaitValue()
        assertThat(value.token).isEqualTo(expectedToken)
    }

    @Test
    fun `test logout`() {
        val expectedResponse = LogoutResponse("user logged out", 200)
        val value = viewModel.logout("thisisanauthtoken").getOrAwaitValue()
        assertThat(value.message).isEqualTo(expectedResponse.message)
        assertThat(value.status).isEqualTo(expectedResponse.status)
    }
}