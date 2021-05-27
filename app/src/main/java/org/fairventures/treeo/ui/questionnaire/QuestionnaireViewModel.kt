package org.fairventures.treeo.ui.questionnaire

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.fairventures.treeo.repositories.DBMainRepository
import org.fairventures.treeo.util.IDispatcherProvider
import org.fairventures.treeo.util.mappers.ModelEntityMapper

class QuestionnaireViewModel @ViewModelInject constructor(
    private val dbMainRepository: DBMainRepository,
    private val dispatcher: IDispatcherProvider,
    private val mapper: ModelEntityMapper
) : ViewModel() {

    fun updateOption(id: Long, isSelected: Boolean) {
        viewModelScope.launch(dispatcher.io()) {
            dbMainRepository.updateOption(id, isSelected)
        }
    }

    fun markActivityAsCompleted(id: Long, isCompleted: Boolean) {
        viewModelScope.launch(dispatcher.io()) {
            dbMainRepository.markActivityAsCompleted(id, isCompleted)
        }
    }

}

