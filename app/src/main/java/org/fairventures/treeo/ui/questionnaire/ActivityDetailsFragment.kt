package org.fairventures.treeo.ui.questionnaire

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_activity_details.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.fairventures.treeo.R
import org.fairventures.treeo.adapters.ActivityDetailsAdapter
import org.fairventures.treeo.adapters.HelperActivityListener
import org.fairventures.treeo.db.models.Activity
import org.fairventures.treeo.db.models.Page
import org.fairventures.treeo.models.ActivityDetail
import javax.inject.Inject

@AndroidEntryPoint
class ActivityDetailsFragment : Fragment(), HelperActivityListener {
    private var selectedLanguage = "en"
    private lateinit var adapter: ActivityDetailsAdapter
    private var plannedActivity: Activity? = null
    private var activityList = mutableListOf<Activity>()
    private var pageList = listOf<Page>()
    private var answerList = mutableListOf<String>()
    private var answerMap = mutableMapOf<String, String>()
    private var qnCode = ""

    @Inject
    lateinit var sharedPref: SharedPreferences

    private val activityDetailsViewModel: ActivityDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityList.clear()
        plannedActivity = arguments?.getParcelable("activity")
//        activityList.add(plannedActivity!!)
        getAnswers()
//        setObservers()
        initializeViews()
    }

    private fun initializeViews() {
        initializeTextViews()
//        initializeRecycler()
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
        adapter.submitList(getActivityDetail(), selectedLanguage)
//        adapter.submitAnswerMap(answerMap)
    }

    private fun getAnswers() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                for (i in plannedActivity!!.questionnaire!!.pages.indices) {
                    qnCode = plannedActivity!!.questionnaire!!.pages[i].questionCode
                    answerMap[qnCode] =
                        activityDetailsViewModel.getAnswers(
                            plannedActivity!!.questionnaire!!.questionnaire_id_from_remote,
                            plannedActivity!!.questionnaire!!.pages[i].questionCode
                        ).answers[0]
                    Log.d("getAns", answerMap.size.toString())
                }
            } catch (e: NullPointerException) {
            }
            initializeRecycler()
        }
    }

    private fun getActivityDetail(): List<ActivityDetail> {
        return listOf(
            ActivityDetail(plannedActivity!!, answerMap)
        )
    }

//    private fun setObservers() {
//        activityDetailsViewModel.answers.observe(viewLifecycleOwner, Observer {
//            if (it != null) {
//                answerMap[qnCode] = it.answers[0]
//                Log.d("getAns", answerMap.size.toString())
//            }
//        })
//    }

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
