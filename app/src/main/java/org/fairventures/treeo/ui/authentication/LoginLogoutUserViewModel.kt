package org.fairventures.treeo.ui.authentication

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import org.fairventures.treeo.models.LoginDetails
import org.fairventures.treeo.repository.MainRepository

class LoginLogoutUserViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository
): ViewModel() {
    fun login(email: String, password: String)
            = mainRepository.login(LoginDetails(email,password))

    fun logout(token: String)
            = mainRepository.logout(token)
}