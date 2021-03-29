package org.fairventures.treeo.ui.authentication

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*
import org.fairventures.treeo.R
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val loginLogoutUserViewModel: LoginLogoutUserViewModel by viewModels()

    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = arguments?.getString("email")

        login_email_input.setText(email)

        login_button.setOnClickListener {
            showProgressBar()
            loginEmailPassword(
                login_email_input.text.toString(),
                login_password_input.text.toString()
            )
        }

        setObservers()
    }

    private fun setObservers() {
        loginLogoutUserViewModel.loginToken.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                saveUserDetails(it.token, getString(R.string.email_password))
                openHome(it.token)
            }
        })
    }

    private fun loginEmailPassword(email: String, password: String) {
        loginLogoutUserViewModel.login(email, password)
    }

    private fun saveUserDetails(token: String, loginManager: String) {
        with(sharedPref.edit()) {
            putString(getString(R.string.user_token), token)
            putString(getString(R.string.loginManager), loginManager)
            apply()
        }
    }

    private fun openHome(extra: String) {
        hideProgressBar()
        this.findNavController().navigate(
            R.id.action_loginFragment_to_homeFragment,
            bundleOf("username" to extra)
        )
    }

    private fun showProgressBar() {
        login_progress_bar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        login_progress_bar.visibility = View.GONE
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}
