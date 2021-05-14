package org.fairventures.treeo.ui.questionnaire

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
    private val entityMapper: ModelEntityMapper
) : ViewModel() {

    private val _activitySummaryItems = MutableLiveData<List<ActivitySummaryItem>>()
    val activitySummaryItems: LiveData<List<ActivitySummaryItem>> get() = _activitySummaryItems

    fun getActivitySummaryItems(activity: Activity) {
        viewModelScope.launch {
            val pagesResult = withContext(dispatcher.io()) {
                getSummaryPages(activity.id)
            }

            val listResult = withContext(dispatcher.io()) {
                addSummaryItemToList(activity, pagesResult)
            }

            _activitySummaryItems.postValue(listResult)
        }
    }

    private suspend fun getSummaryPages(activityId: Long): List<Page> {
        val result = dbMainRepository.getQuestionnairePages(activityId)
        val pages = entityMapper.getListOfPageFromEntities(result.pages)

        pages.forEach { page ->
            val options = dbMainRepository.getPageOptions(page.pageId)
            page.options = entityMapper.getListOfOptionsFromEntities(options)
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

