package org.fairventures.treeo.ui.authentication

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
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

    private val _googleUser = MutableLiveData<GoogleUser>()
    val googleUser: LiveData<GoogleUser> get() = _googleUser

    private val _newUser =  MutableLiveData<NewRegisteredUser>()
    val newUser:  LiveData<NewRegisteredUser> get() = _newUser

    private val _facebookUser = MutableLiveData<FacebookUser>()
    val facebookUser: LiveData<FacebookUser> get() = _facebookUser


    fun createUser(registerUser: RegisterUser){
        viewModelScope.launch(dispatcher.io()) {
            _newUser.postValue(mainRepository.createUser(registerUser))
        }
    }

    fun googleSignUp(googleAuthToken: String){
        viewModelScope.launch(dispatcher.io()) {
            _googleUser.postValue(mainRepository.googleSignUp(googleAuthToken))
        }
    }

    fun facebookSignUp(access_token: String) {
        viewModelScope.launch(dispatcher.io()) {
            _facebookUser.postValue(mainRepository.faceBookSignUp(access_token))
        }
    }
}
