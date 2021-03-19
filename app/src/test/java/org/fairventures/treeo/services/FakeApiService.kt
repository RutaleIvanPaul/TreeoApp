package org.fairventures.treeo.services

import org.fairventures.treeo.models.*
import retrofit2.Response
import retrofit2.mock.BehaviorDelegate

class FakeApiService(private val delegate: BehaviorDelegate<ApiService>) : ApiService {

    override suspend fun createUser(registerUser: RegisterUser): Response<NewRegisteredUser> {
        val user = NewRegisteredUser(
            registerUser.email,
            registerUser.firstName,
            false,
            registerUser.lastName
        )
        return delegate.returningResponse(user).createUser(registerUser)
    }

    override suspend fun googleSignUp(googleAuthToken: GoogleToken): Response<GoogleUser> {
        val googleUser = GoogleUser(
            "test@gmail.com",
            200,
            "thisisatesttoken",
            "tester"
        )
        return delegate.returningResponse(googleUser).googleSignUp(googleAuthToken)
    }

    override suspend fun facebookSignUp(access_token: String): Response<FacebookUser> {
        val facebookUser = FacebookUser(
            "test@gmail.com",
            "firstname",
            "lastname",
            200,
            "thisisatesttoken",
            "username"
        )
        return delegate.returningResponse(facebookUser).facebookSignUp(access_token)
    }

    override suspend fun login(loginDetails: LoginDetails): Response<LoginToken> {
        val loginToken = LoginToken("thisisatesttoken")
        return delegate.returningResponse(loginToken).login(loginDetails)
    }

    override suspend fun logOut(token: String): Response<LogoutResponse> {
        val logoutResponse = LogoutResponse("logout success", 200)
        return delegate.returningResponse(logoutResponse).logOut(token)
    }
}
