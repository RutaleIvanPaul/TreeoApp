package org.fairventures.treeo.ui.authentication.login.screens

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_phone_otp.*
import org.fairventures.treeo.R
import org.fairventures.treeo.models.LoginWithOTP
import org.fairventures.treeo.models.RequestOTP
import org.fairventures.treeo.ui.authentication.LoginLogoutViewModel
import org.fairventures.treeo.util.errors
import javax.inject.Inject


@AndroidEntryPoint
class PhoneOtpFragment : Fragment() {

    private val viewModel: LoginLogoutViewModel by activityViewModels()
    private var phoneNumber = ""

    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_phone_otp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setObservers()
    }

    private fun setUpViews() {
        setUpButtons()
        setUpOTPView()
        setUpResendTextView()
    }

    private fun setUpButtons() {
        phoneLoginOTPBackButton.setOnClickListener {
            viewModel.loginBack()
        }
    }

    private fun setUpOTPView() {
        otpView.setOtpCompletionListener {
            hideOTPProgressBar()
            closeKeyboard(otpView)
            showLoginProgressBar()
            disableBackButton()
            viewModel.loginWithOTP(LoginWithOTP(phoneNumber, it))
        }
    }

    private fun setUpResendTextView() {
        resendPassCodeTextView.setOnClickListener {
            showOTPProgressBar()
            viewModel.requestOTP(RequestOTP(phoneNumber))
        }
    }

    private fun setObservers() {
        viewModel.phoneNumber.observe(viewLifecycleOwner, Observer {
            phoneNumber = it
            val bannerText = resources.getString(R.string.otp_pass_code_message) + " " + phoneNumber
            otpScreenMessageTextView.text = bannerText
        })

        viewModel.smsLoginResponse.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                hideLoginProgressBar()
                saveUserDetails(it.token, it.userId)
                enableBackButton()
                openHome()
            } else {
                hideLoginProgressBar()
                enableBackButton()
                showResendPasscodeText()
                Toast.makeText(requireContext(), errors.value, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveUserDetails(token: String, userId: Int) {
        with(sharedPref.edit()) {
            putString(getString(R.string.user_token), token)
            putInt(getString(R.string.user_id), userId)
            apply()
        }
    }

    private fun openHome() {
        findNavController().navigate(R.id.action_phoneAuthHostFragment_to_homeFragment)
    }

    private fun enableBackButton() {
        phoneLoginOTPBackButton.isEnabled = true
    }

    private fun disableBackButton() {
        phoneLoginOTPBackButton.isEnabled = false
    }

    private fun showOTPProgressBar() {
        otpWaitProgressBar.visibility = View.VISIBLE
    }

    private fun hideOTPProgressBar() {
        otpWaitProgressBar.visibility = View.GONE
    }

    private fun showLoginProgressBar() {
        otpLoginProgressBar.visibility = View.VISIBLE
    }

    private fun hideLoginProgressBar() {
        otpLoginProgressBar.visibility = View.GONE
    }

    private fun showResendPasscodeText() {
        resendPassCodeTextView.visibility = View.VISIBLE
    }

    private fun hideResendPasscodeText() {
        resendPassCodeTextView.visibility = View.GONE
    }

    private fun closeKeyboard(view: View) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        @JvmStatic
        fun newInstance() = PhoneOtpFragment()
    }
}
