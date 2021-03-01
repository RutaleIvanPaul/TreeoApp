package org.fairventures.treeo.ui.authentication

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import org.fairventures.treeo.models.RegisterUser
import org.fairventures.treeo.repository.MainRepository


class RegisterUserViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository
): ViewModel() {
    fun createUser(registerUser: RegisterUser)
            = mainRepository.createUser(registerUser)
}