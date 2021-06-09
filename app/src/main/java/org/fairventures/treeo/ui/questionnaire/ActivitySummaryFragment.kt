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
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_activity_summary.*
import org.fairventures.treeo.R
import org.fairventures.treeo.adapters.ActivitySummaryAdapter
import org.fairventures.treeo.adapters.ActivitySummaryListener
import org.fairventures.treeo.models.Activity
import org.fairventures.treeo.models.ActivitySummaryItem
import javax.inject.Inject

@AndroidEntryPoint
class ActivitySummaryFragment : Fragment(), ActivitySummaryListener {
    @Inject
    lateinit var sharedPref: SharedPreferences
    private val activitySummaryViewModel: ActivitySummaryViewModel by activityViewModels()

    private var selectedLanguage: String? = null
    private var plannedActivity: Activity? = null
    private lateinit var adapter: ActivitySummaryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        plannedActivity = arguments?.getParcelable("activity")
        selectedLanguage = sharedPref.getString("Selected Language", "en")
        getSummaryItems(plannedActivity!!)
        initializeViews()
        setObservers()
    }

    override fun onResume() {
        super.onResume()
        getSummaryItems(plannedActivity!!)

    }

    private fun getSummaryItems(activity: Activity) {
        activitySummaryViewModel.getActivitySummaryItems(activity.id)
    }

    private fun initializeViews() {
        initializeTextViews()
        initializeRecycler()
        initializeButtons()
    }

    private fun initializeTextViews() {
        activityTitleTextView.text = plannedActivity!!.title
        activityDescriptionTextView.text = plannedActivity!!.title
    }

    private fun initializeRecycler() {
        adapter = ActivitySummaryAdapter(requireContext(), this)
        activitySummaryRecyclerview.adapter = adapter
        activitySummaryRecyclerview.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun initializeButtons(){
        btn_continue_to_photos.setOnClickListener {
//            view?.findNavController()
//                ?.navigate(R.id.action_activitySummaryFragment_to_requestCameraFragment)
        }
    }

    private fun setObservers() {
        activitySummaryViewModel.activitySummaryItems.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                selectedLanguage = sharedPref.getString("Selected Language", "en")!!
                updateRecyclerview(it, selectedLanguage!!)
            }
        })
    }

    private fun updateRecyclerview(list: List<ActivitySummaryItem>, language: String) {
        adapter.submitList(list, language)
    }

    override fun onActivityClick(activity: ActivitySummaryItem) {
        if (activity.activity.template.activityType =="land-survey-part-2"){
            findNavController()
                .navigate(
                    R.id.action_activitySummaryFragment_to_requestCameraFragment,
                    bundleOf("activity" to plannedActivity)
                )
        }else {
            findNavController()
                .navigate(
                    R.id.action_activitySummaryFragment_to_questionnaireFragment,
                    bundleOf("summaryItem" to activity)
                )
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ActivitySummaryFragment()
    }
}
