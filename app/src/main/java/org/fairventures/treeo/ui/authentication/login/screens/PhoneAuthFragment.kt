package org.fairventures.treeo.ui.authentication.login.screens

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_phone_auth.*
import org.fairventures.treeo.R
import org.fairventures.treeo.adapters.CountrySpinnerAdapter
import org.fairventures.treeo.models.Country
import org.fairventures.treeo.models.RequestOTP
import org.fairventures.treeo.ui.authentication.LoginLogoutViewModel
import org.fairventures.treeo.util.disableView
import org.fairventures.treeo.util.enableView
import org.fairventures.treeo.util.errors
import org.fairventures.treeo.util.getCountries

@AndroidEntryPoint
class PhoneAuthFragment : Fragment() {

    private val viewModel: LoginLogoutViewModel by activityViewModels()
    private val TAG = "PhoneAuthFragment"

    val countryCode = MutableLiveData<String>()
    val phoneNumber = MutableLiveData<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_phone_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
    }

    private fun initializeViews() {
        createSpinner()
        onTextChangeWatcher()
        setUpButton()
        setObservers()
    }

    private fun setUpButton() {
        phoneLoginContinueButton.setOnClickListener {
            viewModel.loginContinue()
            viewModel.setPhoneNumber(phoneNumber.value.toString())
            viewModel.requestOTP(RequestOTP(phoneNumber.value.toString()))
        }
    }

    private fun createSpinner() {
        val arrayAdapter = CountrySpinnerAdapter(
            requireContext(),
            R.layout.country_spinner_item,
            getCountries()
        )
        countrySpinner.adapter = arrayAdapter
        countrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val country: Country = parent?.getItemAtPosition(position) as Country
                countryCode.postValue(country.code)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun onTextChangeWatcher() {
        phoneInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("bTextChange", s.toString())
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val phone = countryCode.value + s.toString()
                phoneNumber.postValue(phone)
            }

            override fun afterTextChanged(s: Editable?) {
                val phone = countryCode.value + s.toString()
                phoneNumber.postValue(phone)
            }
        })
    }

    private fun setObservers() {
        countryCode.observe(viewLifecycleOwner, {
            if (it != null) {
                phoneTextInputLayout.prefixText = countryCode.value
            }
        })

        phoneNumber.observe(viewLifecycleOwner, {
            if (it != null && it.length == 13) {
                closeKeyboard(phoneInputEditText)
                enableView(phoneLoginContinueButton)
            } else {
                disableView(phoneLoginContinueButton)
            }
        })

        viewModel.phoneNumberOTPResponse.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                enableView(phoneLoginContinueButton)
                disableView(checkNumberProgressBar)
            } else {
                disableView(checkNumberProgressBar)
                Toast.makeText(requireContext(), errors.value, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun closeKeyboard(view: View) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        @JvmStatic
        fun newInstance() = PhoneAuthFragment()
    }
}
