package org.fairventures.treeo.ui.authentication

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.fairventures.treeo.models.FacebookUser
import org.fairventures.treeo.models.GoogleUser
import org.fairventures.treeo.models.NewRegisteredUser
import org.fairventures.treeo.models.RegisterUser
import org.fairventures.treeo.repository.IMainRepository
import org.fairventures.treeo.util.IDispatcherProvider


class RegisterUserViewModel @ViewModelInject constructor(
    private val mainRepository: IMainRepository,
    private val dispatcher: IDispatcherProvider
) :
    ViewModel() {
    fun createUser(registerUser: RegisterUser): MutableLiveData<NewRegisteredUser> {
        val newUser = MutableLiveData<NewRegisteredUser>()
        viewModelScope.launch(dispatcher.io()) {
            newUser.postValue(mainRepository.createUser(registerUser).value)
        }
        return newUser
    }

    fun googleSignUp(googleAuthToken: String): MutableLiveData<GoogleUser> {
        val googleUser = MutableLiveData<GoogleUser>()
        viewModelScope.launch(dispatcher.io()) {
            googleUser.postValue(mainRepository.googleSignUp(googleAuthToken).value)
        }
        return googleUser
    }

    fun facebookSignUp(access_token: String): MutableLiveData<FacebookUser> {
        val facebookUser = MutableLiveData<FacebookUser>()
        viewModelScope.launch(dispatcher.io()) {
            facebookUser.postValue(mainRepository.faceBookSignUp(access_token).value)
        }
        return facebookUser
    }
}
