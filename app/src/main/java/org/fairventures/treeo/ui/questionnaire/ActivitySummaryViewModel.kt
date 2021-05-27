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
        summaryList.add(0,ActivitySummaryItem(activity, pages))
        if(activity.template.activityType == "land-survey"){

        }
        else{
            val activity2 = Activity(
                activity.id,activity.remoteId,activity.dueDate,activity.isCompleted,
                "Photos",activity.description, activity.plot, activity.template)
            val page1 = Page(pages[0].pageId,pages[0].pageType,pages[0].questionCode,
                mapOf("en" to "Land Photos","in" to "Gambar"),pages[0].description,pages[0].options)
            val page2 = Page(pages[0].pageId,pages[0].pageType,pages[0].questionCode,
                mapOf("en" to "Soil Photos","in" to "Gambar"),pages[0].description,pages[0].options)
            activity2.template.activityType = "land-survey-part-2"
            val pages2 = listOf<Page>(page1, page2)
            summaryList.add(1,ActivitySummaryItem(activity2, pages2))
        }
        return summaryList
    }
}

