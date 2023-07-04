package org.fairventures.treeo.ui.home.screens

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*
import org.fairventures.treeo.R
import org.fairventures.treeo.ui.authentication.LoginLogoutViewModel
import org.fairventures.treeo.util.errors
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    @Inject
    lateinit var sharedPref: SharedPreferences

    private val loginLogoutViewModel: LoginLogoutViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeButton()
        setObservers()
    }

    private fun setObservers() {
        loginLogoutViewModel.logoutResponse.observe(viewLifecycleOwner, Observer { logoutResponse ->
            if (logoutResponse != null) {
                deleteUserDetailsFromSharePref()
            } else {
                Toast.makeText(context, errors.value, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun initializeButton() {
        profileLogoutButton.setOnClickListener {
            logoutUser()
        }
    }

    private fun logoutUser() {
        logoutFromBackend()
    }

    private fun logoutFromBackend() {
        with(sharedPref.edit()) {
            val token = sharedPref.getString(getString(R.string.user_token), null)
            if (!token.isNullOrEmpty()) {
                loginLogoutViewModel.logout(token)
            }
            apply()
        }
    }

    private fun deleteUserDetailsFromSharePref() {
        with(sharedPref.edit()) {
            remove(getString(R.string.user_token))
            apply()
        }
        closeApp()
    }

    private fun closeApp() {
        requireActivity().finish()
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}
