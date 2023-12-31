package org.fairventures.treeo.ui.authentication.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.shuhart.stepview.StepView
import dagger.hilt.android.AndroidEntryPoint
import org.fairventures.treeo.R
import org.fairventures.treeo.adapters.PhoneAuthPagerAdapter
import org.fairventures.treeo.ui.authentication.LoginLogoutViewModel
import org.fairventures.treeo.ui.authentication.login.screens.PhoneAuthFragment
import org.fairventures.treeo.ui.authentication.login.screens.PhoneOtpFragment

@AndroidEntryPoint
class PhoneAuthHostFragment : Fragment() {

    private val viewModel: LoginLogoutViewModel by activityViewModels()

    private lateinit var viewPager: ViewPager2
    private lateinit var stepView: StepView

    private lateinit var adapter: PhoneAuthPagerAdapter

    private val fragments = listOf(
        PhoneAuthFragment(),
        PhoneOtpFragment()
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_phone_auth_host, container, false)
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
        adapter = PhoneAuthPagerAdapter(
            fragments,
            requireActivity().supportFragmentManager,
            lifecycle
        )
        viewPager = view.findViewById(R.id.phoneAuthViewPager)
        viewPager.isUserInputEnabled = false
        viewPager.adapter = adapter
        setUpViewPagerIndicator(view)
    }

    private fun setUpViewPagerIndicator(view: View) {
        stepView = view.findViewById(R.id.phoneRegistrationIndicatorView)
        stepView.state
            .animationType(StepView.ANIMATION_LINE)
            .stepsNumber(fragments.size)
            .animationDuration(resources.getInteger(android.R.integer.config_shortAnimTime))
            .commit()
    }

    private fun setObservers() {
        viewModel.loginStep.observe(viewLifecycleOwner, Observer {
            viewPager.currentItem = it
            stepView.go(it, true)
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = PhoneAuthHostFragment()
    }
}
