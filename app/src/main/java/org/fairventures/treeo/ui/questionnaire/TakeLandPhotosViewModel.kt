package org.fairventures.treeo.ui.questionnaire

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.fairventures.treeo.db.models.LandSurvey
import org.fairventures.treeo.repositories.DBMainRepository

class TakeLandPhotosViewModel @ViewModelInject constructor(
    private val dbMainRepository: DBMainRepository
): ViewModel() {

    fun insertLandSurveyItem(landSurvey: LandSurvey){
        viewModelScope.launch {
            dbMainRepository.insertLandSurvey(landSurvey)
        }
    }

}