package org.fairventures.treeo.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.lifecycle.Observer
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import org.fairventures.treeo.R
import org.fairventures.treeo.models.RegisterUser
import org.fairventures.treeo.ui.Home.HomeActivity
import org.fairventures.treeo.ui.authentication.LoginLogoutUserViewModel
import org.fairventures.treeo.ui.authentication.RegisterUserViewModel
import org.fairventures.treeo.util.RC_SIGN_IN
import org.fairventures.treeo.util.errors
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val registerUserViewModel: RegisterUserViewModel by viewModels()
    private val loginLogoutUserViewModel: LoginLogoutUserViewModel by viewModels()
    val callbackManager = CallbackManager.Factory.create()

    @Inject
    lateinit var sharedPref: SharedPreferences

    @Inject
    lateinit var googleSignInOptions: GoogleSignInOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        sign_in_button.setSize(SignInButton.SIZE_STANDARD)
        sign_in_button.setOnClickListener {
            val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        val loginButton = login_button_facebook as LoginButton
        loginButton.setPermissions(Arrays.asList("email", "public_profile"))

        // Callback registration
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                Log.d("FB Token", loginResult?.accessToken?.token!!)
                registerUserViewModel.facebookSignUp(loginResult.accessToken?.token!!)
            }

            override fun onCancel() {
                // App code
            }

            override fun onError(exception: FacebookException) {
                textView1.setText("Cancelled")
            }
        })

        register_user_button.setOnClickListener {
            registerUserViewModel.createUser(
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

        login_logout_toggle.setOnClickListener {
            toggleLoginUI(loginButton)
        }

        login_button_email_password.setOnClickListener {
            loginEmailPassword(email.text.toString(), password.text.toString())
        }

        errors.observe(this, Observer {
            textView1.setText(it)
        })

        setObservers(loginButton)

    }

    private fun setObservers(loginButton: LoginButton) {

        registerUserViewModel.googleUser.observe(this, Observer { googleUser ->
            Log.d("Google Observer", "Now in Google Observer!!!!")
            if (googleUser != null) {
                Log.d("Google Connect", googleUser.userName)
                saveUserDetails(googleUser.token, getString(R.string.google))
                openHome(googleUser.userName)
            } else {
                Log.d("Google Connect", "Google object is null.")
            }
        })


        registerUserViewModel.newUser.observe(this, Observer { registeredUser ->
            if (registeredUser != null) {
                toggleLoginUI(loginButton)
                email.setText(registeredUser.email)
                password.text.clear()
            }
        })

        registerUserViewModel.facebookUser.observe(
                this,
                Observer { facebookReturn ->
                    if (facebookReturn != null) {
                        saveUserDetails(
                                facebookReturn.token,
                                getString(R.string.facebook)
                        )
                        openHome(
                                facebookReturn.firstName
                        )
                    }
                }
        )

        loginLogoutUserViewModel.loginToken.observe(this, Observer { loginToken ->
                    if (loginToken != null) {
                        saveUserDetails(loginToken.token, getString(R.string.email_password))
                        openHome(loginToken.userName)
                    }
                    else{
                        Log.d("Login Token","Login token is null!!!")
                    }
                })


    }

    private fun toggleLoginUI(loginButton: LoginButton) {
        if (firstname.isInvisible) {
            login_logout_toggle.setText(R.string.already_account_login)
            login_button_email_password.visibility = View.GONE
            loginButton.visibility = View.VISIBLE
            register_user_button.visibility = View.VISIBLE
            sign_in_button.visibility = View.VISIBLE
            firstname.visibility = View.VISIBLE
            lastname.visibility = View.VISIBLE
            country.visibility = View.VISIBLE
            register_username.visibility = View.VISIBLE
            phone.visibility = View.VISIBLE
        } else if (firstname.isShown) {
            login_logout_toggle.setText(R.string.no_account_sign_up)
            login_button_email_password.visibility = View.VISIBLE
            loginButton.visibility = View.INVISIBLE
            register_user_button.visibility = View.INVISIBLE
            sign_in_button.visibility = View.INVISIBLE
            firstname.visibility = View.INVISIBLE
            lastname.visibility = View.INVISIBLE
            country.visibility = View.INVISIBLE
            register_username.visibility = View.INVISIBLE
            phone.visibility = View.INVISIBLE
        }
    }

    override fun onStart() {
        super.onStart()

        //Check if user has signed in before.

        val account = GoogleSignIn.getLastSignedInAccount(this)

        val accessToken: AccessToken? = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired()


        val user_token = sharedPref.getString(getString(R.string.user_token), "")

        if (!sharedPref.getString(getString(R.string.loginManager), null).isNullOrEmpty()) {
            if (account != null) {
                Log.d("Sign In Details", account.idToken!!)
                openHome(account.displayName!!)
            } else if (isLoggedIn) {
                openHome(Profile.getCurrentProfile().name)
            } else if (!user_token.isNullOrBlank()) {
                openHome(user_token)
            }
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
                textView1.setText("Could not sign in")

            } else {
                Log.d("Sign In Details", account.idToken!!)
                registerUserViewModel.googleSignUp(account.idToken!!)
            }

        } catch (e: ApiException) {
            Log.d("TAG", "signInResult:failed code=" + e.statusCode)
            textView1.setText("signInResult:failed code = ${e.statusCode}")
        }
    }

    private fun saveUserDetails(token: String, loginManager: String) {
        with(sharedPref.edit()) {
            putString(getString(R.string.user_token), token)
            putString(getString(R.string.loginManager), loginManager)
            apply()
        }
    }

    private fun openHome(extra: String) {
        val intent = Intent(this, HomeActivity::class.java).apply {
            putExtra("username", extra)
        }
        startActivity(intent)
    }

    private fun loginEmailPassword(email: String, password: String) {
        loginLogoutUserViewModel.login(email, password)
    }
}
