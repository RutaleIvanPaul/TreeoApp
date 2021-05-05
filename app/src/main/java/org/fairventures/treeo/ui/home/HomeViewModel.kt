package org.fairventures.treeo.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.fairventures.treeo.db.models.Activity
import org.fairventures.treeo.db.models.QuestionnaireAnswer
import org.fairventures.treeo.repository.DBMainRepository
import org.fairventures.treeo.util.IDispatcherProvider

class HomeViewModel @ViewModelInject constructor(
    private val dbMainRepository: DBMainRepository,
    private val dispatcher: IDispatcherProvider
): ViewModel() {

    fun insertActivity(activity: Activity){
        viewModelScope.launch(dispatcher.io()){
            dbMainRepository.insertActivity(activity)
        }
    }

    fun getAllActivities() =
        dbMainRepository.getActivities()

    fun insertQuestionnaireAnswer(questionnaireAnswer: QuestionnaireAnswer){
        viewModelScope.launch(dispatcher.io()) {
            dbMainRepository.insertQuestionnaireAnswer(questionnaireAnswer)
        }
    }

    fun getQuestionnaireAnswers(questionnaire_id_from_remote: Long) =
        dbMainRepository.getQuestionnaireAnswers(questionnaire_id_from_remote)

    fun getAnsweredQuestion(questionnaire_id_from_remote: Long, questionCode: String) =
        dbMainRepository.getAnsweredQuestion(questionnaire_id_from_remote,questionCode)


}
