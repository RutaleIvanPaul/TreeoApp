package org.fairventures.treeo.ui.questionnaire

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.shuhart.stepview.StepView
import kotlinx.android.synthetic.main.fragment_questionnaire.*
import org.fairventures.treeo.R
import org.fairventures.treeo.adapters.QuestionnaireRecyclerAdapter
import org.fairventures.treeo.db.models.Activity
import org.fairventures.treeo.db.models.Page

class QuestionnaireFragment : Fragment() {

    private val viewModel: QuestionnaireViewModel by activityViewModels()
    private var plannedActivity: Activity? = null
    private var currentPage: Int = 0
    private lateinit var pages: Array<Page>
    private lateinit var questionAdapter: QuestionnaireRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_questionnaire, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        plannedActivity = arguments?.getParcelable("activity")
        setUpViews()
    }

    private fun setUpViews() {
        initializePages()
        initializeStepView()
        initializeTextView()
        initializeButtons()
        initializeRecycler()
    }

    private fun initializeTextView() {
        val title = pages[currentPage].header
        questionnaireTextView.text = title["en"]
    }

    private fun initializePages() {
        pages = plannedActivity!!.questionnaire!!.pages
    }

    private fun initializeStepView() {
        questionnaireIndicatorView.state
            .animationType(StepView.ANIMATION_LINE)
            .stepsNumber(pages.size)
            .animationDuration(resources.getInteger(android.R.integer.config_shortAnimTime))
            .commit()
    }

    private fun initializeRecycler() {
        questionAdapter = QuestionnaireRecyclerAdapter()
        questionnaireRecyclerView.adapter = questionAdapter
        questionnaireRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        questionAdapter.submitList(pages[currentPage].options.toList(), pages[currentPage].pageType)
    }

    private fun initializeButtons() {
        questionnaireContinueButton.setOnClickListener {
            if (currentPage < pages.size - 1) {
                currentPage += 1
                updateStepView()
                updateTextView()
                updateRecyclerView()
            }
        }

        questionnaireBackButton.setOnClickListener {
            if (currentPage > 0) {
                currentPage -= 1
                updateStepView()
                updateTextView()
                updateRecyclerView()
            }
        }
    }

    private fun updateStepView() {
        questionnaireIndicatorView.go(currentPage, true)
    }

    private fun updateTextView() {
        questionnaireTextView.text = pages[currentPage].header["en"]
    }

    private fun updateRecyclerView() {
        questionAdapter.submitList(pages[currentPage].options.toList(), pages[currentPage].pageType)
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = QuestionnaireFragment()
    }
}
