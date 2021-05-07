package org.fairventures.treeo.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.fairventures.treeo.db.models.Activity
import org.fairventures.treeo.db.models.QuestionnaireAnswer
import org.fairventures.treeo.models.UserActivities
import org.fairventures.treeo.repository.DBMainRepository
import org.fairventures.treeo.repository.MainRepository
import org.fairventures.treeo.util.IDispatcherProvider


class HomeViewModel @ViewModelInject constructor(
    private val dbMainRepository: DBMainRepository,
    private val dispatcher: IDispatcherProvider,
    private val mainRepository: MainRepository
): ViewModel() {

    private val _nextTwoActivities = MutableLiveData<List<Activity>>()
    val nextTwoActivities: LiveData<List<Activity>> get() = _nextTwoActivities

    private val _plannedActivities = MutableLiveData<UserActivities>()
    val plannedActivities: LiveData<UserActivities> get() = _plannedActivities

    fun insertActivity(activity: Activity) {
        viewModelScope.launch(dispatcher.io()) {
            dbMainRepository.insertActivity(activity)
        }
    }

    fun getAllActivities() = dbMainRepository.getActivities()

    fun insertQuestionnaireAnswer(questionnaireAnswer: QuestionnaireAnswer) {
        viewModelScope.launch(dispatcher.io()) {
            dbMainRepository.insertQuestionnaireAnswer(questionnaireAnswer)
        }
    }

    fun getQuestionnaireAnswers(questionnaire_id_from_remote: Long) =
        dbMainRepository.getQuestionnaireAnswers(questionnaire_id_from_remote)

    fun getPlannedActivities(token: String){
        viewModelScope.launch(dispatcher.io()) {
            _plannedActivities.postValue(mainRepository.retrievePlannedActivities(token))
        }
    }


    fun getNextTwoActivities() {
        viewModelScope.launch(dispatcher.io()) {
            _nextTwoActivities.postValue(dbMainRepository.getNextTwoActivities())
        }
    }
}



