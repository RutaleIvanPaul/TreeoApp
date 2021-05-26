package org.fairventures.treeo.ui.home.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_guide.*
import org.fairventures.treeo.R
import org.fairventures.treeo.adapters.GuideRecyclerAdapter
import org.fairventures.treeo.adapters.OnGuideClickListener
import org.fairventures.treeo.models.Activity
import org.fairventures.treeo.ui.home.GuideViewModel

@AndroidEntryPoint
class GuideFragment : Fragment(), OnGuideClickListener {

    private val guideViewModel: GuideViewModel by viewModels()

    private var activityList = listOf<Activity>()
    private val adapter = GuideRecyclerAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guide, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCompletedActivities()
        setUpViews()
        setObservers()
    }

    private fun setUpViews() {
        initializeRecycler()
    }

    private fun initializeRecycler() {
        guideRecyclerView.adapter = adapter
        guideRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun getCompletedActivities() {
        guideViewModel.getCompletedActivities()
    }

    private fun setObservers() {
        guideViewModel.completedActivities.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                updateRecyclerview(it)
            }
        })
    }

    private fun updateRecyclerview(list: List<Activity>) {
        adapter.submitList(list)
    }


    override fun onClick(activity: Activity) {
        findNavController()
            .navigate(
                R.id.action_guideFragment_to_activityDetailsFragment,
                bundleOf("activity" to activity)
            )
    }

    companion object {
        @JvmStatic
        fun newInstance() = GuideFragment()
    }
}
