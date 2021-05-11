package org.fairventures.treeo.ui.authentication.registration.screens

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_user_phone.*
import org.fairventures.treeo.R
import org.fairventures.treeo.adapters.CountrySpinnerAdapter
import org.fairventures.treeo.models.Country
import org.fairventures.treeo.ui.authentication.GDPRFragment
import org.fairventures.treeo.ui.authentication.RegistrationViewModel
import org.fairventures.treeo.util.*
import javax.inject.Inject

@AndroidEntryPoint
class UserPhoneFragment : Fragment() {

    private val viewModel: RegistrationViewModel by activityViewModels()

    val countryCode = MutableLiveData<String>()
    val phoneNumber = MutableLiveData<String>()
    var country = ""
    private var gdprConsent = MutableLiveData<Boolean>()

    private val TAG = "UserPhoneFragment"

    @Inject
    lateinit var sharedPref: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_phone, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews(view)
        setObservers()
    }

    private fun initializeViews(view: View) {
        initializeButtons(view)
        initializeSpinner()
        initializeEditText()
        onCheckboxClicked()
        setUpGDPRConsent()
    }

    private fun initializeSpinner() {
        val arrayAdapter = CountrySpinnerAdapter(
            requireContext(),
            R.layout.country_spinner_item,
            getCountries()
        )
        userPhoneCountrySpinner.adapter = arrayAdapter
        userPhoneCountrySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    val country: Country = parent?.getItemAtPosition(position) as Country
                    countryCode.postValue(country.code)
                    setUserCountry(country.code)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
    }

    private fun setUserCountry(countryCode: String) {
        when (countryCode) {
            "+256" -> {
                country = "Uganda"
            }
            "+62" -> {
                country = "Indonesia"
            }
            "+420" -> {
                country = "Czech Republic"
            }
            "+49" -> {
                country = "Germany"
            }
        }
    }

    private fun initializeEditText() {
        userPhoneInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val phone = countryCode.value + s.toString()
                phoneNumber.postValue(phone)
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

    private fun setUpGDPRConsent() {
        text_gdpr.setOnClickListener {
            GDPRFragment.display(childFragmentManager)
        }
    }

    private fun onCheckboxClicked() {
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                gdprConsent.postValue(true)
            } else {
                gdprConsent.postValue(false)
            }
        }
    }

    private fun initializeButtons(view: View) {
        userPhoneContinueButton.setOnClickListener {
            viewModel.setUserPhoneNumber(phoneNumber.value.toString(), country)
            viewModel.setGDPRStatus(gdprConsent.value!!)
            viewModel.registerMobileUser()
            viewModel.registrationContinue()
        }

        userPhoneBackButton.setOnClickListener {
            viewModel.registrationBack()
        }

        userPhoneLoginLink.setOnClickListener {
            setFirstTimeUserStatus()
            view.findNavController().navigate(
                R.id.action_registrationHostFragment_to_phoneAuthHostFragment
            )
        }
    }

    private fun setObservers() {
        countryCode.observe(viewLifecycleOwner, {
            if (it != null) {
                userPhoneTextInputLayout.prefixText = countryCode.value
            }
        })

        phoneNumber.observe(viewLifecycleOwner, Observer {
            if (it != null && it.length == 13) {
                closeKeyboard(userPhoneInputEditText, requireContext())
                hideView(userPhoneLoginLink)
                showView(userPhoneProgressBar)
                viewModel.validatePhoneNumberRegistration(
                    phoneNumber.value?.removePrefix("+").toString()
                )
            }
        })

        gdprConsent.observe(viewLifecycleOwner, Observer {
            if (it != false && phoneNumber.value != null) {
                enableView(userPhoneContinueButton)
            } else {
                disableView(userPhoneContinueButton)
            }
        })

        viewModel.phoneNumberValidationResponseRegistration.observe(viewLifecycleOwner, Observer {
            hideView(userPhoneProgressBar)
            if (it != null) {
                showView(userPhoneLoginLink)
            } else {
                hideView(userPhoneLoginLink)
                Toast.makeText(context, errors.value, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setFirstTimeUserStatus() {
        with(sharedPref.edit()) {
            putBoolean(getString(R.string.first_time_user), false)
            apply()
        }
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = UserPhoneFragment()
    }
}
