package org.fairventures.treeo.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.fairventures.treeo.db.models.Activity
import org.fairventures.treeo.repository.DBMainRepository
import org.fairventures.treeo.util.IDispatcherProvider

class GuideViewModel @ViewModelInject constructor(
    private val dbMainRepository: DBMainRepository,
    private val dispatcher: IDispatcherProvider,
) : ViewModel() {

    private val _completedActivities = MutableLiveData<List<Activity>>()
    val completedActivities: LiveData<List<Activity>> get() = _completedActivities

    fun getCompleteActivities() {
        viewModelScope.launch(dispatcher.io()) {
            _completedActivities.postValue(dbMainRepository.getCompletedActivities())
        }

    }
}
