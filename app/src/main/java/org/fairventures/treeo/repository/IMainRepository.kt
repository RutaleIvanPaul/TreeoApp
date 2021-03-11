package org.fairventures.treeo.repository

import androidx.lifecycle.MutableLiveData
import org.fairventures.treeo.models.*

interface IMainRepository {
    fun createUser(registerUser: RegisterUser): MutableLiveData<NewRegisteredUser>
    fun googleSignUp(googleAuthToken: String): MutableLiveData<GoogleUser>
    fun faceBookSignUp(accessToken: String): MutableLiveData<FacebookUser>
    fun login(loginDetails: LoginDetails): MutableLiveData<LoginToken>
    fun logout(token: String): MutableLiveData<LogoutResponse>
}