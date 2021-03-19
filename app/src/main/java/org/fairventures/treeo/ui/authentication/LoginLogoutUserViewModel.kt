package org.fairventures.treeo.ui.authentication

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.fairventures.treeo.models.LoginDetails
import org.fairventures.treeo.models.LoginToken
import org.fairventures.treeo.models.LogoutResponse
import org.fairventures.treeo.repository.IMainRepository
import org.fairventures.treeo.util.IDispatcherProvider

class LoginLogoutUserViewModel @ViewModelInject constructor(
    private val mainRepository: IMainRepository,
    private val dispatcher: IDispatcherProvider
) : ViewModel() {
    fun login(email: String, password: String): MutableLiveData<LoginToken> {
        val loginToken = MutableLiveData<LoginToken>()
        viewModelScope.launch(dispatcher.io()) {
            loginToken.postValue(mainRepository.login(LoginDetails(email, password)).value)
        }
        return loginToken
    }

    fun logout(token: String): MutableLiveData<LogoutResponse> {
        val logoutResponse = MutableLiveData<LogoutResponse>()
        viewModelScope.launch(dispatcher.io()) {
            logoutResponse.postValue(mainRepository.logout(token).value)
        }
        return logoutResponse
    }
}
