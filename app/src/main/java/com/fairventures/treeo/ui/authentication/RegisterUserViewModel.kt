package com.fairventures.treeo.ui.authentication

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.fairventures.treeo.repository.MainRepository


class RegisterUserViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository
): ViewModel() {

}