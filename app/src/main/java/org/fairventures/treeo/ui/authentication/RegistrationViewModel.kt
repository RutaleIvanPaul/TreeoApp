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


class RegistrationViewModel @ViewModelInject constructor(
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

    private val _phoneNumberValidationResponseRegistration =
        MutableLiveData<ValidateResponseData>()
    val phoneNumberValidationResponseRegistration: LiveData<ValidateResponseData>
        get() = _phoneNumberValidationResponseRegistration

    private val _registeredMobileUser = MutableLiveData<RegisteredMobileUser>()
    val registeredMobileUser: LiveData<RegisteredMobileUser> get() = _registeredMobileUser

    private val _validateOTPRegistrationResponse =
        MutableLiveData<ValidateOTPRegistrationResponse>()
    val validateOTPRegistrationResponse: LiveData<ValidateOTPRegistrationResponse>
        get() =
            _validateOTPRegistrationResponse

    private val _onBoardingStep = MutableLiveData(0)
    val onBoardingStep: LiveData<Int> get() = _onBoardingStep

    private val _registrationStep = MutableLiveData(0)
    val registrationStep: LiveData<Int> get() = _registrationStep

    private val _localeLanguage = MutableLiveData<String>()
    val localeLanguage: LiveData<String> get() = _localeLanguage

    private val _phoneNumber = MutableLiveData<String>()
    val phoneNumber: LiveData<String> get() = _phoneNumber

    val motivationList = MutableLiveData<MutableList<String>>()
    private val motivations = mutableListOf<String>()

    private val newUserObj = RegisterMobileUser()


    fun createUser(registerUser: RegisterUser) {
        viewModelScope.launch(dispatcher.io()) {
            _newUser.postValue(mainRepository.createUser(registerUser))
        }
    }

    fun validatePhoneNumberRegistration(phoneNumber: String) {
        viewModelScope.launch(dispatcher.io()) {
            _phoneNumberValidationResponseRegistration.postValue(
                mainRepository.validatePhoneNumber(
                    phoneNumber
                )
            )
        }
    }

    fun registerMobileUser(mobileUser: RegisterMobileUser) {
        viewModelScope.launch(dispatcher.io()) {
            _registeredMobileUser.postValue((mainRepository.registerMobileUser(mobileUser)))
        }
    }

    fun validateOTPRegistration(validateOTPRegistration: ValidateOTPRegistration) {
        viewModelScope.launch(dispatcher.io()) {
            _validateOTPRegistrationResponse.postValue(
                mainRepository.validateOTPRegistration(
                    validateOTPRegistration
                )
            )
        }
    }

    fun setLocaleLanguage(language: String) {
        _localeLanguage.value = language
    }

    fun registerMobileUser() {
        registerMobileUser(newUserObj)
    }

    fun setUserInformation(firstName: String, lastName: String) {
        newUserObj.firstName = firstName
        newUserObj.lastName = lastName
    }

    fun setUserPhoneNumber(phoneNumber: String, country: String) {
        _phoneNumber.value = phoneNumber
        newUserObj.phoneNumber = phoneNumber
        newUserObj.country = country
    }

    fun setGDPRStatus(check_gdpr: Boolean) {
        newUserObj.isGdprCompliant = check_gdpr
    }

    fun getNewUserObj() = newUserObj

    fun onBoardingContinue() {
        _onBoardingStep.value = onBoardingStep.value!!.plus(1)
    }

    fun onBoardingBack() {
        _onBoardingStep.value = onBoardingStep.value?.minus(1)
    }

    fun resetOnBoardingStep() {
        _onBoardingStep.value = 0
    }

    fun registrationContinue() {
        _registrationStep.value = registrationStep.value!!.plus(1)
    }

    fun registrationBack() {
        _registrationStep.value = registrationStep.value!!.minus(1)
    }

    fun resetRegistrationStep() {
        _registrationStep.value = 0
    }

    fun addMotivation(motivation: String) {
        motivations.add(motivation)
        motivationList.value = motivations
    }

    fun removeMotivation(motivation: String) {
        if (motivations.contains(motivation)) {
            motivations.remove(motivation)
            motivationList.value = motivations
        }
    }

    fun clearMotivations() {
        if (motivations.size > 0) {
            motivations.clear()
            motivationList.value = motivations
        }
    }
}
