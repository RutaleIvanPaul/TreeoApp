package org.fairventures.treeo.ui.authentication

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.fairventures.treeo.models.DeviceInformation
import org.fairventures.treeo.models.LoginDetails
import org.fairventures.treeo.models.LoginToken
import org.fairventures.treeo.models.LogoutResponse
import org.fairventures.treeo.repository.IMainRepository
import org.fairventures.treeo.util.IDispatcherProvider

class LoginLogoutUserViewModel @ViewModelInject constructor(
    private val mainRepository: IMainRepository,
    private val dispatcher: IDispatcherProvider
) : ViewModel() {

    private val _loginToken = MutableLiveData<LoginToken>()
    val loginToken: LiveData<LoginToken> get() = _loginToken

    private val _logoutResponse = MutableLiveData<LogoutResponse>()
    val logoutResponse: LiveData<LogoutResponse> get() = _logoutResponse

    fun login(email: String, password: String) {
        viewModelScope.launch(dispatcher.io()) {
            _loginToken.postValue(mainRepository.login(LoginDetails(email, password)))
        }
    }

    fun logout(token: String){
        viewModelScope.launch(dispatcher.io()) {
            _logoutResponse.postValue(mainRepository.logout(token))
        }
    }

    fun postDeviceData(deviceInformation: DeviceInformation, userToken: String){
        viewModelScope.launch(dispatcher.io()) {
            mainRepository.postDeviceData(deviceInformation, userToken)
        }
    }
}
