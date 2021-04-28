package org.fairventures.treeo.ui.authentication.registration.screens

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_user_activation.*
import org.fairventures.treeo.R
import org.fairventures.treeo.models.ValidateOTPRegistration
import org.fairventures.treeo.ui.authentication.RegistrationViewModel
import org.fairventures.treeo.util.closeKeyboard
import org.fairventures.treeo.util.errors
import org.fairventures.treeo.util.hideView
import org.fairventures.treeo.util.showView
import javax.inject.Inject

@AndroidEntryPoint
class UserActivationFragment : Fragment() {

    @Inject
    lateinit var preferences: SharedPreferences

    private val viewModel: RegistrationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_activation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        setObservers(view)
    }

    private fun initializeViews() {
        initializeTextView()
        initializeButtons()
        initializeOtpView()
    }

    private fun initializeTextView() {
        viewModel.phoneNumber.observe(viewLifecycleOwner, Observer {
            val bannerText = resources.getString(R.string.otp_pass_code_message) + " " + it
            userActivationScreenMessageTextView.text = bannerText
        })
    }

    private fun initializeButtons() {
        userActivationBackButton.setOnClickListener {
            viewModel.registrationBack()
        }
    }

    private fun initializeOtpView() {
        userActivationOtpView.setOtpCompletionListener {
            showView(userActivationOtpProgressBar)
            hideView(userActivationOtpWaitProgressBar)
            closeKeyboard(userActivationOtpView, requireContext())
            viewModel.validateOTPRegistration(
                ValidateOTPRegistration(
                    it,
                    viewModel.getNewUserObj().phoneNumber
                )
            )
        }
    }

    private fun setObservers(view: View) {
        viewModel.registeredMobileUser.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                saveUserInfoToPrefs(it.firstName, it.id)
            }
        })

        viewModel.validateOTPRegistrationResponse.observe(viewLifecycleOwner, Observer {
            hideView(userActivationOtpProgressBar)
            if (it != null) {
                view.findNavController()
                    .navigate(R.id.action_registrationHostFragment_to_homeFragment)
            } else {
                Toast.makeText(context, errors.value, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveUserInfoToPrefs(firstName: String, userId: Int, token: String = "") {
        with(preferences.edit()) {
            putBoolean(getString(org.fairventures.treeo.R.string.first_time_user), false)
            putString("firstName", firstName)
            putInt(getString(R.string.user_id), userId)
            putString(getString(R.string.user_token), token)
            apply()
        }
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = UserActivationFragment()
    }
}
