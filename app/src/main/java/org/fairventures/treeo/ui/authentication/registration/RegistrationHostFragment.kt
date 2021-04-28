package org.fairventures.treeo.ui.authentication.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.shuhart.stepview.StepView
import org.fairventures.treeo.R
import org.fairventures.treeo.adapters.MainPagerAdapter
import org.fairventures.treeo.ui.authentication.RegistrationViewModel
import org.fairventures.treeo.ui.authentication.registration.screens.UserActivationFragment
import org.fairventures.treeo.ui.authentication.registration.screens.UserInfoFragment
import org.fairventures.treeo.ui.authentication.registration.screens.UserPhoneFragment

class RegistrationHostFragment : Fragment() {

    private val viewModel: RegistrationViewModel by activityViewModels()

    private lateinit var viewPager: ViewPager2
    private lateinit var stepView: StepView

    private lateinit var pagerAdapter: MainPagerAdapter

    private val fragments = listOf(
        UserInfoFragment(),
        UserPhoneFragment(),
        UserActivationFragment()
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration_host, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews(view)
        setObservers()
    }

    private fun initializeViews(view: View) {
        setUpViewPager(view)
    }

    private fun setUpViewPager(view: View) {
        pagerAdapter = MainPagerAdapter(
            fragments,
            requireActivity().supportFragmentManager,
            lifecycle
        )
        viewPager = view.findViewById(R.id.registrationViewPager)
        viewPager.isUserInputEnabled = false
        viewPager.adapter = pagerAdapter
        setUpViewPagerIndicator(view)
    }

    private fun setUpViewPagerIndicator(view: View) {
        stepView = view.findViewById(R.id.registrationIndicatorView)
        stepView.state
            .animationType(StepView.ANIMATION_LINE)
            .stepsNumber(fragments.size)
            .animationDuration(resources.getInteger(android.R.integer.config_shortAnimTime))
            .commit()
    }

    private fun setObservers() {
        viewModel.registrationStep.observe(viewLifecycleOwner, Observer {
            viewPager.currentItem = it
            stepView.go(it, true)
        })
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = RegistrationHostFragment()
    }
}
