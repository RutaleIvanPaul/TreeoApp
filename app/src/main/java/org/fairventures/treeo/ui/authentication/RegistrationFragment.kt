package org.fairventures.treeo.ui.authentication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_registration.*
import org.fairventures.treeo.R
import org.fairventures.treeo.models.RegisterUser
import org.fairventures.treeo.util.RC_SIGN_IN
import org.fairventures.treeo.util.errors
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationFragment : Fragment() {
    private val registrationViewModel: RegistrationViewModel by viewModels()
    private val callbackManager: CallbackManager = CallbackManager.Factory.create()

    @Inject
    lateinit var sharedPref: SharedPreferences

    @Inject
    lateinit var googleSignInOptions: GoogleSignInOptions

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setObservers()
        signInWithGoogle()
        signInWithFacebook()
        userRegistration()
    }

    private fun setUpViews() {
        login_screen_link.setOnClickListener {
            openLogin("")
        }

        phoneNumberLogin.setOnClickListener {
            openSMSValidationPage()
        }

        regOnBoardStatusBtn.setOnClickListener {
            alterOnBoardStatus()
        }
    }

    private fun setObservers() {
        registrationViewModel.googleUser.observe(viewLifecycleOwner, Observer { googleUser ->
            Log.d("Google Observer", "Now in Google Observer!!!!")
            if (googleUser != null) {
                Log.d("Google Connect", googleUser.userName)
                saveUserDetails(googleUser.token, getString(R.string.google))
                openHome(googleUser.userName)
            } else {
                Log.d("Google Connect", "Google object is null.")
            }
        })

        registrationViewModel.newUser.observe(viewLifecycleOwner, Observer { registeredUser ->
            if (registeredUser != null) {
                openLogin(registeredUser.email)
            } else {
                hideProgressBar()
                registrationBanner.text = errors.value.toString()
            }
        })

        registrationViewModel.facebookUser.observe(
            viewLifecycleOwner,
            Observer { facebookReturn ->
                if (facebookReturn != null) {
                    saveUserDetails(facebookReturn.token, getString(R.string.facebook))
                    openHome(facebookReturn.firstName)
                }
            }
        )
    }

    private fun signInWithGoogle() {
        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)

        sign_in_button.setSize(SignInButton.SIZE_STANDARD)
        sign_in_button.setOnClickListener {
            showProgressBar()
            val signInIntent: Intent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    private fun signInWithFacebook() {
        val loginButton = login_button_facebook as LoginButton
        loginButton.setPermissions(listOf("email", "public_profile"))
        loginButton.fragment = this

        // Callback registration
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                showProgressBar()
                registrationViewModel.facebookSignUp(loginResult!!.accessToken.token)
            }

            override fun onCancel() {
                // App code
            }

            override fun onError(exception: FacebookException) {
//                textView1.text = "Cancelled"
                Log.d("fbresult", exception.message.toString())
            }
        })
    }

    private fun userRegistration() {
        register_user_button.setOnClickListener {
            showProgressBar()
            registrationViewModel.createUser(
                RegisterUser(
                    firstname.text.toString(),
                    lastname.text.toString(),
                    password.text.toString(),
                    email.text.toString(),
                    country.text.toString(),
                    register_username.text.toString(),
                    phone.text.toString()
                )
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            if (account == null) {
                Log.d("Sign In Details", "Could Not Sign In")
                registrationBanner.text = "Could not sign in"

            } else {
                Log.d("Sign In Details", account.idToken!!)
                registrationViewModel.googleSignUp(account.idToken!!)
            }
        } catch (e: ApiException) {
            Log.d("TAG", "signInResult:failed code=" + e.statusCode)
            registrationBanner.text = "signInResult:failed codes = ${e.statusCode}"
        }
    }

    private fun saveUserDetails(token: String, loginManager: String) {
        with(sharedPref.edit()) {
            putString(getString(R.string.user_token), token)
            putString(getString(R.string.loginManager), loginManager)
            apply()
        }
    }

    private fun openSMSValidationPage() {
        showProgressBar()
//        this.findNavController().navigate(
//            R.id.action_registrationFragment_to_SMSCodeFragment
//        )
    }

    private fun openHome(extra: String) {
        hideProgressBar()
//        this.findNavController().navigate(
//            R.id.action_registrationFragment_to_homeFragment,
//            bundleOf("username" to extra)
//        )
    }

    private fun openLogin(extra: String) {
        if (registration_progress_bar.isVisible) {
            hideProgressBar()
        }
//        this.findNavController().navigate(
//            R.id.action_registrationFragment_to_loginFragment,
//            bundleOf("email" to extra)
//        )
    }

    private fun alterOnBoardStatus() {
        with(sharedPref.edit()) {
            putBoolean(getString(R.string.first_time_user), true)
            apply()
        }
    }

    private fun showProgressBar() {
        registration_progress_bar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        registration_progress_bar.visibility = View.GONE
    }

    companion object {
        @JvmStatic
        fun newInstance() = RegistrationFragment()
    }
}
