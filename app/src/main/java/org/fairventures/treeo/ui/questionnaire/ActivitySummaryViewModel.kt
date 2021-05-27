package org.fairventures.treeo.ui.questionnaire

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.fairventures.treeo.models.Activity
import org.fairventures.treeo.models.ActivitySummaryItem
import org.fairventures.treeo.models.Page
import org.fairventures.treeo.repositories.DBMainRepository
import org.fairventures.treeo.util.IDispatcherProvider
import org.fairventures.treeo.util.mappers.ModelEntityMapper

class ActivitySummaryViewModel @ViewModelInject constructor(
    private val dbMainRepository: DBMainRepository,
    private val dispatcher: IDispatcherProvider,
    private val mapper: ModelEntityMapper
) : ViewModel() {

    private val _activitySummaryItems = MutableLiveData<List<ActivitySummaryItem>>()
    val activitySummaryItems: LiveData<List<ActivitySummaryItem>> get() = _activitySummaryItems

    fun getActivitySummaryItems(activityId: Long) {
        viewModelScope.launch {
            val activityResult = withContext(dispatcher.io()) {
                getActivity(activityId)
            }

            val pagesResult = withContext(dispatcher.io()) {
                getSummaryPages(activityResult.id)
            }

            val listResult = withContext(dispatcher.io()) {
                addSummaryItemToList(activityResult, pagesResult)
            }

            _activitySummaryItems.postValue(listResult)
        }
    }

    private suspend fun getActivity(activityId: Long): Activity {
        return mapper.getActivityFromEntity(dbMainRepository.getActivity(activityId))
    }

    private suspend fun getSummaryPages(activityId: Long): List<Page> {
        val result = dbMainRepository.getQuestionnairePages(activityId)
        val pages = mapper.getListOfPageFromEntities(result.pages)

        pages.forEach { page ->
            val options = dbMainRepository.getPageOptions(page.pageId)
            page.options = mapper.getListOfOptionsFromEntities(options)
        }
        return pages
    }

    private fun addSummaryItemToList(
        activity: Activity,
        pages: List<Page>
    ): List<ActivitySummaryItem> {
        val summaryList = mutableListOf<ActivitySummaryItem>()
        summaryList.add(ActivitySummaryItem(activity, pages))
        return summaryList
    }
}

