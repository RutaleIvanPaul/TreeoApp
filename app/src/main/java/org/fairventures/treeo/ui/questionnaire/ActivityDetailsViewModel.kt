package org.fairventures.treeo.ui.questionnaire

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.fairventures.treeo.db.models.QuestionnaireAnswer
import org.fairventures.treeo.repository.DBMainRepository
import org.fairventures.treeo.util.IDispatcherProvider

class ActivityDetailsViewModel @ViewModelInject constructor(
    private val dbMainRepository: DBMainRepository,
    private val dispatcher: IDispatcherProvider
) : ViewModel() {

    private val _answers = MutableLiveData<QuestionnaireAnswer>()
    val answers: LiveData<QuestionnaireAnswer> get() = _answers

//    fun getAnswers(qnRemoteId: Long, qnCode: String) {
//        viewModelScope.launch(dispatcher.io()) {
//            _answers.postValue(dbMainRepository.getAnsweredQuestion(qnRemoteId, qnCode))
//        }
//    }

    suspend fun getAnswers(qnRemoteId: Long, qnCode: String) =
        dbMainRepository.getAnsweredQuestion(qnRemoteId, qnCode)
}

