package com.fairventures.treeo.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.fairventures.treeo.repository.MainRepository

class GetFarmersViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    fun getFarmers() = mainRepository.getFarmersfromApi()
}