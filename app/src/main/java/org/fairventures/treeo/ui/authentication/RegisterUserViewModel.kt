package org.fairventures.treeo.ui.authentication

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.fairventures.treeo.models.FacebookUser
import org.fairventures.treeo.models.GoogleUser
import org.fairventures.treeo.models.NewRegisteredUser
import org.fairventures.treeo.models.RegisterUser
import org.fairventures.treeo.repository.MainRepository


class RegisterUserViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    fun createUser(registerUser: RegisterUser): MutableLiveData<NewRegisteredUser> {
        val newUser = MutableLiveData<NewRegisteredUser>()
        viewModelScope.launch(Dispatchers.IO) {
            newUser.postValue(mainRepository.createUser(registerUser).value)
        }
        return newUser
    }

    fun googleSignUp(googleAuthToken: String): MutableLiveData<GoogleUser> {
        val googleUser = MutableLiveData<GoogleUser>()
        viewModelScope.launch(Dispatchers.IO) {
            googleUser.postValue(mainRepository.googleSignUp(googleAuthToken).value)
        }
        return googleUser
    }

    fun facebookSignUp(access_token: String): MutableLiveData<FacebookUser> {
        val facebookUser = MutableLiveData<FacebookUser>()
        viewModelScope.launch(Dispatchers.IO) {
            facebookUser.postValue(mainRepository.faceBookSignUp(access_token).value)
        }
        return facebookUser
    }
}