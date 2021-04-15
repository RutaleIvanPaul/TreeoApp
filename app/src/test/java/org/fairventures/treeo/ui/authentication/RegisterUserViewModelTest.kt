package org.fairventures.treeo.ui.authentication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.fairventures.treeo.MainCoroutineRule
import org.fairventures.treeo.models.*
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

    @Test
    fun `test validatePhoneNumber_Registration returns ValidateResponseData`() =
        mainCoroutineRule.testDispatcher.runBlockingTest {
            val expectedNumber = "111"
            viewModel.validatePhoneNumber_Registration(expectedNumber)
            assertThat(viewModel.phoneNumberValidationResponse_registration.value?.phoneNumber)
                    .isEqualTo(expectedNumber)
        }

    @Test
    fun `test registerMobileUser returns RegisteredMobileUser` () =
            mainCoroutineRule.testDispatcher.runBlockingTest {
                val mobileUser = RegisterMobileUser(
                        firstName = "firstname",
                        lastName = "lastname",
                        country = "Uganda",
                        password = "password",
                        phoneNumber = "123",
                        username = "username"
                )
                viewModel.registerMobileUser(
                        mobileUser
                )
                assertThat(viewModel.registeredMobileUser.value?.firstName)
                        .isEqualTo(mobileUser.firstName)
            }

    @Test
    fun `test validateOTPRegistration returns ValidateOTPRegistrationResponse`() =
            mainCoroutineRule.testDispatcher.runBlockingTest {
                val validateOTPRegistration = ValidateOTPRegistration(
                        code = "123",
                        phoneNumber = "123"
                )
                viewModel.validateOTPRegistration(
                        validateOTPRegistration
                )
                assertThat(viewModel.validateOTPRegistrationResponse.value?.message)
                        .isEqualTo("User Active")
            }
}

