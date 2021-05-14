package org.fairventures.treeo.ui.questionnaire

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shuhart.stepview.StepView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_questionnaire.*
import org.fairventures.treeo.R
import org.fairventures.treeo.adapters.OptionCheckedListener
import org.fairventures.treeo.adapters.OptionRecyclerAdapter
import org.fairventures.treeo.models.ActivitySummaryItem
import org.fairventures.treeo.models.Page
import javax.inject.Inject


@AndroidEntryPoint
class QuestionnaireFragment : Fragment(), OptionCheckedListener {
    @Inject
    lateinit var sharedPref: SharedPreferences

    private val questionnaireViewModel: QuestionnaireViewModel by activityViewModels()
    private var summaryItem: ActivitySummaryItem? = null
    private var currentPage: Int = 0
    private var pageList = listOf<Page>()
    private lateinit var questionAdapter: OptionRecyclerAdapter

    private var selectedLanguage = "en"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        summaryItem = arguments?.getParcelable("summaryItem")
        return inflater.inflate(R.layout.fragment_questionnaire, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedLanguage = getSelectedLanguage()
        pageList = summaryItem!!.pages
        initializeViews()
    }

    private fun initializeViews() {
        initializeStepView()
        initializeTextViews()
        initializeButtons()
        initializeRecycler()
    }

    private fun initializeStepView() {
        questionnaireIndicatorView.state
            .animationType(StepView.ANIMATION_LINE)
            .stepsNumber(pageList.size)
            .animationDuration(resources.getInteger(android.R.integer.config_shortAnimTime))
            .commit()
    }

    private fun initializeTextViews() {
        questionnaireTextView.text = pageList[currentPage].header[selectedLanguage]
        questionnaireDescriptionTextView.text = pageList[currentPage].description[selectedLanguage]
    }

    private fun initializeRecycler() {
        questionAdapter = OptionRecyclerAdapter(selectedLanguage, this)
        optionRecyclerview.adapter = questionAdapter
        optionRecyclerview.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        questionAdapter.submitList(pageList[currentPage].options!!, pageList[currentPage].pageType)
    }

    private fun initializeButtons() {
        questionnaireContinueButton.setOnClickListener {
            if (currentPage < pageList.size - 1) {
                currentPage += 1
                updateStepView()
                updateTextViews()
                updateRecyclerView()
            } else if (currentPage == pageList.size - 1) {
                completeActivity(summaryItem!!.activity.id)
                leaveQuestionnaire()
            }
        }
    }

    private fun leaveQuestionnaire() {
        findNavController().popBackStack()
    }

    private fun updateStepView() {
        questionnaireIndicatorView.go(currentPage, true)
    }

    private fun updateTextViews() {
        questionnaireTextView.text = pageList[currentPage].header[selectedLanguage]
        questionnaireDescriptionTextView.text = pageList[currentPage].description[selectedLanguage]
    }

    private fun updateRecyclerView() {
        questionAdapter.submitList(pageList[currentPage].options!!, pageList[currentPage].pageType)
    }

    private fun getSelectedLanguage(): String {
        with(sharedPref.edit()) {
            return sharedPref.getString("Selected Language", "en")!!
        }
    }

    private fun completeActivity(id: Long) {
        questionnaireViewModel.markActivityAsCompleted(id)
    }

    override fun onOptionCheck(id: Long, isSelected: Boolean) {
        questionnaireViewModel.updateOption(id, isSelected)
    }

    companion object {

        @JvmStatic
        fun newInstance() = QuestionnaireFragment()
    }
}


