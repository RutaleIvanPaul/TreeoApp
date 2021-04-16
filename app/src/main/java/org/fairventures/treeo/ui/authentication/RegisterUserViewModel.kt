package org.fairventures.treeo.ui.authentication

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.fairventures.treeo.models.*
import org.fairventures.treeo.repository.IMainRepository
import org.fairventures.treeo.util.IDispatcherProvider


class RegisterUserViewModel @ViewModelInject constructor(
    private val mainRepository: IMainRepository,
    private val dispatcher: IDispatcherProvider
) :
    ViewModel() {
    private val _googleUser = MutableLiveData<GoogleUser>()
    val googleUser: LiveData<GoogleUser> get() = _googleUser

    private val _newUser = MutableLiveData<NewRegisteredUser>()
    val newUser: LiveData<NewRegisteredUser> get() = _newUser

    private val _facebookUser = MutableLiveData<FacebookUser>()
    val facebookUser: LiveData<FacebookUser> get() = _facebookUser

    private val _phoneNumberValidationResponse_registration = MutableLiveData<ValidateResponseData>()
    val phoneNumberValidationResponse_registration: LiveData<ValidateResponseData> get() =
        _phoneNumberValidationResponse_registration

    private val _registeredMobileUser = MutableLiveData<RegisteredMobileUser>()
    val registeredMobileUser: LiveData<RegisteredMobileUser> get() = _registeredMobileUser

    private val _validateOTPRegistrationResponse = MutableLiveData<ValidateOTPRegistrationResponse>()
    val validateOTPRegistrationResponse: LiveData<ValidateOTPRegistrationResponse> get() =
        _validateOTPRegistrationResponse


    fun createUser(registerUser: RegisterUser) {
        viewModelScope.launch(dispatcher.io()) {
            _newUser.postValue(mainRepository.createUser(registerUser))
        }
    }

    fun googleSignUp(googleAuthToken: String) {
        viewModelScope.launch(dispatcher.io()) {
            _googleUser.postValue(mainRepository.googleSignUp(googleAuthToken))
        }
    }

    fun facebookSignUp(access_token: String) {
        viewModelScope.launch(dispatcher.io()) {
            _facebookUser.postValue(mainRepository.faceBookSignUp(access_token))
        }
    }

    fun validatePhoneNumber_Registration(phoneNumber: String){
        viewModelScope.launch(dispatcher.io()) {
            _phoneNumberValidationResponse_registration.postValue(mainRepository.validatePhoneNumber(phoneNumber))
        }
    }

    fun registerMobileUser(mobileUser: RegisterMobileUser){
        viewModelScope.launch (dispatcher.io()){
            _registeredMobileUser.postValue((mainRepository.registerMobileUser(mobileUser)))
        }
    }

    fun validateOTPRegistration(validateOTPRegistration: ValidateOTPRegistration){
        viewModelScope.launch(dispatcher.io()) {
            _validateOTPRegistrationResponse.postValue(mainRepository.validateOTPRegistration(
                    validateOTPRegistration
            ))
        }
    }
}
