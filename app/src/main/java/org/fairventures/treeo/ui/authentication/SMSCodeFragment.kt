package org.fairventures.treeo.ui.authentication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_s_m_s_code.*
import org.fairventures.treeo.R
import org.fairventures.treeo.models.RegisterMobileUser
import org.fairventures.treeo.models.ValidateOTPRegistration
import org.fairventures.treeo.util.RESOLVE_HINT
import org.fairventures.treeo.util.errors
import javax.inject.Inject

@AndroidEntryPoint
class SMSCodeFragment : Fragment() {

    private val registerUserViewModel: RegisterUserViewModel by viewModels()
    private val loginLogoutUserViewModel: LoginLogoutUserViewModel by viewModels()

    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_s_m_s_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestHint()
        setupUI()
        setupObservers()
    }

    private fun setupUI() {
        login_phoneNumber_button.setOnClickListener {
            showProgressBar()
            validatePhoneNumber(login_phonenumber_text.text.toString())
        }

        signup_sms.setOnClickListener {
            showProgressBar()
            registerUserViewModel.registerMobileUser(
                RegisterMobileUser(
                    firstName = firstname_sms.text.toString(),
                    lastName = lastname_sms.text.toString(),
                    phoneNumber = phone_sms.text.toString(),
                    country = country_sms.text.toString(),
                    password = password_sms.text.toString(),
                    username = username_sms.text.toString()
                )
            )

            saveMobileUserDetails(username_sms.text.toString())
//            startSMSListener()
        }

        validate_OTP.setOnClickListener {
            registerUserViewModel.validateOTPRegistration(
                ValidateOTPRegistration(
                    phoneNumber = phone_sms.text.toString(),
                    code = editText_validateCode.text.toString()
                )
            )
        }
    }

    private fun setupObservers() {
        loginLogoutUserViewModel.phoneNumberOTPResponse.observe(
            viewLifecycleOwner,
            Observer { phoneNumberLoginResponse ->
                Log.d("PhoneNumberResponse", phoneNumberLoginResponse.toString())
            }
        )

        registerUserViewModel.phoneNumberValidationResponse.observe(
            viewLifecycleOwner,
            Observer { validatePhoneNumberResponse ->
                if (validatePhoneNumberResponse != null) {
                    hideProgressBar()
                    if (validatePhoneNumberResponse.valid) {
                        //requestOTP
                        requestOTP(validatePhoneNumberResponse.phoneNumber)
                        startSMSListener()
                    } else {
                        openRegistrationPage()
                    }
                    Log.d("Phone Number Response", validatePhoneNumberResponse.toString())
                }
            }
        )

        registerUserViewModel.registeredMobileUser.observe(
            viewLifecycleOwner,
            Observer { registeredMobileUser ->
                if (registeredMobileUser != null) {
                    hideProgressBar()
                    smsCode.text = "Success"
                }
            }
        )

        errors.observe(
            viewLifecycleOwner,
            Observer {
                hideProgressBar()
                smsCode.text = it
            }
        )

        registerUserViewModel.validateOTPRegistrationResponse.observe(
            viewLifecycleOwner,
            { validateOTPRegistrationResponse ->
                if (validateOTPRegistrationResponse != null) {
                    hideProgressBar()
                    smsCode.setText(validateOTPRegistrationResponse.message)
                    if (validateOTPRegistrationResponse.message.contains("activ", true)) {
                        login_phoneNumber_button.visibility = View.VISIBLE
                        login_phonenumber_text.visibility = View.VISIBLE
                        smsProgressBar.visibility = View.GONE
                    }
                }
            }
        )

    }

    private fun openHomePage() {
        val username = with(sharedPref.edit()) {
            sharedPref.getString(getString(R.string.mobile_username), null)
        } ?: username_sms.text.toString()
        this.findNavController().navigate(
            R.id.action_SMSCodeFragment_to_homeFragment,
            bundleOf(username to "username")
        )
    }

    private fun openRegistrationPage() {
        this.findNavController().navigate(
            R.id.action_SMSCodeFragment_to_registrationFragment
        )
    }

    private fun saveMobileUserDetails(username: String) {
        with(sharedPref.edit()) {
            putString(getString(org.fairventures.treeo.R.string.mobile_username), username)
            apply()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RESOLVE_HINT) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val credential = data?.getParcelableExtra<Credential>(Credential.EXTRA_KEY)
                login_phonenumber_text.setText(credential?.id.toString())
                phone_sms.setText(credential?.id.toString())
            }
        }

    }

    private fun validatePhoneNumber(phoneNumber: String) {
        registerUserViewModel.validatePhoneNumber(phoneNumber.removePrefix("+"))
    }

    private fun requestOTP(phoneNumber: String) {
        loginLogoutUserViewModel.requestOTP(phoneNumber)
    }

    // Construct a request for phone numbers and show the picker
    private fun requestHint() {
        val hintRequest = HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .build()
        val intent = Credentials.getClient(requireActivity()).getHintPickerIntent(hintRequest)
        startIntentSenderForResult(
            intent.intentSender,
            RESOLVE_HINT, null, 0, 0, 0, null
        )
    }

    private fun startSMSListener() {
        val client = SmsRetriever.getClient(requireActivity())
        val task: Task<Void> = client.startSmsRetriever()

        task.addOnSuccessListener(OnSuccessListener<Void?> {
            // Successfully started retriever, expect broadcast intent
            Log.d("MESSAGE", "Successfully started retriever, expect broadcast intent")
        })

        task.addOnFailureListener(OnFailureListener {
            hideProgressBar()
            // Failed to start retriever, inspect Exception for more details
            Log.d("MESSAGE", "Failed to start retriever, inspect Exception for more details")
        })
    }

    private fun showProgressBar() {
        smsProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        smsProgressBar.visibility = View.GONE
    }

    companion object {
        @JvmStatic
        fun newInstance() = SMSCodeFragment()
    }
}