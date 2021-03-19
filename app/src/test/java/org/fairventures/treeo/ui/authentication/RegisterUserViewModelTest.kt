package org.fairventures.treeo.ui.authentication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.fairventures.treeo.MainCoroutineRule
import org.fairventures.treeo.models.FacebookUser
import org.fairventures.treeo.models.GoogleUser
import org.fairventures.treeo.models.NewRegisteredUser
import org.fairventures.treeo.models.RegisterUser
import org.fairventures.treeo.repository.FakeMainRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RegisterUserViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: RegisterUserViewModel

    @Before
    fun setup() {
        viewModel = RegisterUserViewModel(
            FakeMainRepository(),
            mainCoroutineRule.testDispatcherProvider
        )
    }

    @Test
    fun `test createUser returns NewRegisteredUser`() =
        mainCoroutineRule.testDispatcher.runBlockingTest {
            val user = RegisterUser(
                "firstName",
                "lastName",
                "secret",
                "test@gmail.com",
                "Uganda",
                "test-user",
                "99999"
            )

            val expectedUser = NewRegisteredUser(
                "test@gmail.com",
                "firstName",
                false,
                "lastName"
            )

            viewModel.createUser(user)
            assertThat(viewModel.newUser.value?.email).isEqualTo(expectedUser.email)
        }

    @Test
    fun `test googleSignUp returns GoogleUser`() =
        mainCoroutineRule.testDispatcher.runBlockingTest {
            val testToken = "thisisafaketesttoken"
            val expectedUser = GoogleUser(
                    "username",
                    "googleuser@gmail.com",
                    "thisisanotherfaketoken",
                    0
            )
            viewModel.googleSignUp(testToken)
            assertThat(viewModel.googleUser.value?.email).isEqualTo(expectedUser.email)
        }

    @Test
    fun `test facebookSignUp returns FacebookUser`() =
        mainCoroutineRule.testDispatcher.runBlockingTest {
            val testToken = "thisisafaketesttoken"
            val expectedUser = FacebookUser(
                "face@gmail.com",
                "firstName",
                "lastName",
                0,
                "thisisanotherfaketoken",
                "username"
            )
            viewModel.facebookSignUp(testToken)
            assertThat(viewModel.facebookUser.value?.email).isEqualTo(expectedUser.email)
        }
}
