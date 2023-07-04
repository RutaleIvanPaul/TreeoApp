package org.fairventures.treeo.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.fairventures.treeo.models.Activity
import org.fairventures.treeo.repositories.DBMainRepository
import org.fairventures.treeo.util.IDispatcherProvider
import org.fairventures.treeo.util.mappers.ModelEntityMapper

class GuideViewModel @ViewModelInject constructor(
    private val dbMainRepository: DBMainRepository,
    private val dispatcher: IDispatcherProvider,
    private val mapper: ModelEntityMapper
) : ViewModel() {

    private val _completedActivities = MutableLiveData<List<Activity>>()
    val completedActivities: LiveData<List<Activity>> get() = _completedActivities

    fun getCompletedActivities() {
        viewModelScope.launch(dispatcher.io()) {
            val activityList = mapper.getListOfActivitiesFromEntities(
                dbMainRepository.getCompletedActivities()
            )
            _completedActivities.postValue(activityList)
        }
    }
}
