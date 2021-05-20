package org.fairventures.treeo.ui.questionnaire

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_activity_details.*
import org.fairventures.treeo.R
import org.fairventures.treeo.adapters.ActivityDetailsAdapter
import org.fairventures.treeo.adapters.HelperActivityListener
import org.fairventures.treeo.db.models.Activity
import javax.inject.Inject

class ActivityDetailsFragment : Fragment(), HelperActivityListener {
    private var selectedLanguage = "en"
    private lateinit var adapter: ActivityDetailsAdapter
    private var plannedActivity: Activity? = null
    private var activityList = mutableListOf<Activity>()

    @Inject
    lateinit var sharedPref: SharedPreferences

    private val questionnaireViewModel: QuestionnaireViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        plannedActivity = arguments?.getParcelable("activity")
        activityList.add(plannedActivity!!)
        initializeViews()
        setObservers()
    }

    private fun initializeViews() {
        initializeTextViews()
        initializeRecycler()
    }

    private fun initializeTextViews() {
        activityTitleTextView.text = plannedActivity!!.title
        activityDescriptionTextView.text = plannedActivity!!.title
    }

    private fun initializeRecycler() {
        adapter = ActivityDetailsAdapter(requireContext(), this)
        activityDetailsRecyclerview.adapter = adapter
        activityDetailsRecyclerview.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        adapter.submitList(activityList, selectedLanguage)
    }

    private fun setObservers() {
        questionnaireViewModel.answers.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                adapter.submitAnswerList(it.answers.toList())
            }
        })
    }

    override fun onActivityClick(activity: Activity) {
        findNavController()
            .navigate(
                R.id.action_activityDetailsFragment_to_questionnaireFragment,
                bundleOf("activity" to activity)
            )
    }

    companion object {
        @JvmStatic
        fun newInstance() = ActivityDetailsFragment()
    }
}
