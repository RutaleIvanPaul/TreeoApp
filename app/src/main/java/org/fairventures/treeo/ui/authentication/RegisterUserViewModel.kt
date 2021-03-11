package org.fairventures.treeo.ui.authentication

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import org.fairventures.treeo.models.RegisterUser
import org.fairventures.treeo.repository.IMainRepository
import org.fairventures.treeo.repository.MainRepository


class RegisterUserViewModel @ViewModelInject constructor(
    private val mainRepository: IMainRepository
): ViewModel() {
    fun createUser(registerUser: RegisterUser)
            = mainRepository.createUser(registerUser)

    fun googleSignUp(googleAuthToken: String)
            =mainRepository.googleSignUp(googleAuthToken)

    fun facebookSignUp(access_token: String)
            = mainRepository.faceBookSignUp(access_token)
}