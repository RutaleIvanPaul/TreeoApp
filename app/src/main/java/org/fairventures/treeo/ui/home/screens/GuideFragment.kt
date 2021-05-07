package org.fairventures.treeo.ui.home.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.fairventures.treeo.R
import org.fairventures.treeo.models.Activity
import org.fairventures.treeo.ui.home.HomeViewModel

@AndroidEntryPoint
class GuideFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guide, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
        initializeRecycler()
    }

    private fun initializeRecycler() {
//        guideRecyclerView.adapter = GuideRecyclerAdapter(requireContext(), getActivitiesList())
//        guideRecyclerView.layoutManager = LinearLayoutManager(
//            context,
//            LinearLayoutManager.VERTICAL,
//            false
//        )
    }

    private fun getActivitiesList(): List<Activity> {
        return listOf(
            Activity(
                "Define Your Land",
                getString(R.string.lorem_ipsum),
                "Wed 10th, May",
                R.drawable.trees_2
            ),
            Activity(
                "Prepare Your Land",
                getString(R.string.lorem_ipsum),
                "Thur 10th, June",
                R.drawable.trees_1
            ),
            Activity(
                "Plant Your Seeds",
                getString(R.string.lorem_ipsum),
                "Tue 10th, July",
                R.drawable.trees_3
            ),
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = GuideFragment()
    }
}
