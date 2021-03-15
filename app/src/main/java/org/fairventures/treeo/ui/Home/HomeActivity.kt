package org.fairventures.treeo.ui.Home

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_home.*
import org.fairventures.treeo.R
import org.fairventures.treeo.ui.MainActivity
import org.fairventures.treeo.ui.authentication.LoginLogoutUserViewModel
import javax.inject.Inject


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPref: SharedPreferences

    @Inject
    lateinit var googleSignInOptions: GoogleSignInOptions

    private val loginLogoutUserViewModel: LoginLogoutUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        welcomeMessage.setText("Welcome ${intent.getStringExtra("username")}")

        getDeviceInformation()

        logout_button.setOnClickListener {
            logoutUser()
        }
    }

    private fun logoutUser() {
        val loginManager = sharedPref.getString(getString(R.string.loginManager), "")
        if (loginManager.equals(getString(R.string.google))) {
            signOutGoogle()
        } else if (loginManager.equals(getString(R.string.facebook))) {
            LoginManager.getInstance().logOut()
            logoutFromBackend()
        } else {
            logoutFromBackend()
        }
    }

    private fun backToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun logoutFromBackend() {
        with(sharedPref.edit()) {
            val token = sharedPref.getString(getString(R.string.user_token), null)
            if (!token.isNullOrEmpty()) {
                loginLogoutUserViewModel.logout(token).observe(
                    this@HomeActivity,
                    Observer {
                        if (it != null) {
                            deleteUserDetailsfromSharePref()
                            backToMain()
                        }
                    }
                )
            }
        }
    }

    private fun deleteUserDetailsfromSharePref() {
        with(sharedPref.edit()) {
            remove(getString(R.string.user_token))
            remove(getString(R.string.loginManager))
            apply()
        }
    }

    private fun signOutGoogle() {
        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this, object : OnCompleteListener<Void?> {
                override fun onComplete(task: Task<Void?>) {
                    logoutFromBackend()
                }
            })
    }
}
