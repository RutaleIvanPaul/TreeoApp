package org.fairventures.treeo.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.fairventures.treeo.models.Activity
import org.fairventures.treeo.repositories.DBMainRepository
import org.fairventures.treeo.repositories.MainRepository
import org.fairventures.treeo.services.models.ActivityDTO
import org.fairventures.treeo.util.IDispatcherProvider
import org.fairventures.treeo.util.mappers.ModelEntityMapper


class HomeViewModel @ViewModelInject constructor(
    private val dbMainRepository: DBMainRepository,
    private val dispatcher: IDispatcherProvider,
    private val mainRepository: MainRepository,
    private val entityMapper: ModelEntityMapper
) : ViewModel() {

    private val _plannedActivities = MutableLiveData<List<ActivityDTO>>()
    val plannedActivities: LiveData<List<ActivityDTO>> get() = _plannedActivities

    private val _nextTwoActivities = MutableLiveData<List<Activity>>()
    val nextTwoActivities: LiveData<List<Activity>> get() = _nextTwoActivities

    init {
        getNextTwoActivities()
    }

    fun insertActivity(activities: List<ActivityDTO>) {
        viewModelScope.launch(dispatcher.io()) {
            dbMainRepository.insertActivity(activities)
        }
    }

//    fun getAllActivities() = dbMainRepository.getActivities()

//    fun insertQuestionnaireAnswer(questionnaireAnswer: QuestionnaireAnswer) {
//        viewModelScope.launch(dispatcher.io()) {
//            dbMainRepository.insertQuestionnaireAnswer(questionnaireAnswer)
//        }
//    }

//    fun getQuestionnaireAnswers(questionnaire_id_from_remote: Long) =
//        dbMainRepository.getQuestionnaireAnswers(questionnaire_id_from_remote)

    fun getPlannedActivities(token: String) {
        viewModelScope.launch(dispatcher.io()) {
            _plannedActivities.postValue(mainRepository.retrievePlannedActivities(token))
        }
    }

    private fun getNextTwoActivities() {
        viewModelScope.launch {
            dbMainRepository.getNextTwoActivities().collect { entities ->
                val list = entityMapper.getListOfActivitiesFromEntities(entities)
                _nextTwoActivities.postValue(list)
            }
        }
    }

}
