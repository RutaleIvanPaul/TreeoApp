package org.fairventures.treeo.ui.authentication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import org.fairventures.treeo.getOrAwaitValue
import org.fairventures.treeo.models.FacebookUser
import org.fairventures.treeo.models.GoogleUser
import org.fairventures.treeo.models.NewRegisteredUser
import org.fairventures.treeo.models.RegisterUser
import org.fairventures.treeo.repository.FakeMainRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RegisterUserViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RegisterUserViewModel

    @Before
    fun setup() {
        viewModel = RegisterUserViewModel(FakeMainRepository())
    }

    @Test
    fun `test createUser returns NewRegisteredUser`() {
        val user = RegisterUser(
            "firstName",
            "lastName",
            "secret",
            "test@gmail.com",
            "Uganda",
            "test-user",
            "0759111222"
        )

        val expectedUser = NewRegisteredUser(
            "test@gmail.com",
            "firstName",
            false,
            "lastName"
        )

        val value = viewModel.createUser(user).getOrAwaitValue()
        assertThat(value.email).isEqualTo(expectedUser.email)
        assertThat(value.firstName).isEqualTo(expectedUser.firstName)
        assertThat(value.lastName).isEqualTo(expectedUser.lastName)
    }

    @Test
    fun `test googleSignUp returns GoogleUser`() {
        val testToken = "thisisafaketesttoken"
        val expectedUser = GoogleUser(
            "googleuser@gmail.com",
            0,
            "thisisanotherfaketoken",
            "username"
        )
        val value = viewModel.googleSignUp(testToken).getOrAwaitValue()
        assertThat(value.email).isEqualTo(expectedUser.email)
        assertThat(value.status).isEqualTo(expectedUser.status)
        assertThat(value.userName).isEqualTo(expectedUser.userName)
    }

    @Test
    fun `test facebookSignUp returns FacebookUser`() {
        val testToken = "thisisafaketesttoken"
        val expectedUser = FacebookUser(
            "face@gmail.com",
            "firstName",
            "lastName",
            0,
            "thisisanotherfaketoken",
            "username"
        )
        val value = viewModel.facebookSignUp(testToken).getOrAwaitValue()
        assertThat(value.email).isEqualTo(expectedUser.email)
        assertThat(value.firstName).isEqualTo(expectedUser.firstName)
        assertThat(value.lastName).isEqualTo(expectedUser.lastName)
        assertThat(value.statusCode).isEqualTo(expectedUser.statusCode)
        assertThat(value.username).isEqualTo(expectedUser.username)
    }
}