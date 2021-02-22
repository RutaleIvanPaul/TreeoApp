package com.fairventures.treeo.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.fairventures.treeo.R
import com.fairventures.treeo.models.RegisterUser
import com.fairventures.treeo.ui.authentication.RegisterUserViewModel
import com.fairventures.treeo.util.GOOGLE_CLIENT_ID
import com.fairventures.treeo.util.RC_SIGN_IN
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val registerUserViewModel: RegisterUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(GOOGLE_CLIENT_ID)
            .requestProfile()
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        sign_in_button.setSize(SignInButton.SIZE_STANDARD)
        sign_in_button.setOnClickListener{
            val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }


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
            ).observe(this, Observer {
                textView1.setText(it.toString())
            })

        }

    }

    override fun onStart() {
        super.onStart()

        //Check if user has signed in before.

        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null){
            Log.d("Sign In Details",account.idToken!!)
            textView1.setText("Signed in as  ${account.displayName}")
            sign_in_button.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            if(account == null){
                Log.d("Sign In Details", "Could Not Sign In")
                Toast.makeText(this, "Could Not Sign In", Toast.LENGTH_LONG).show()

            }
            else{
                Log.d("Sign In Details",account.idToken!!)
                textView1.setText("Signed in as  ${account.displayName}")
            }

        } catch (e: ApiException) {
            Log.d("TAG", "signInResult:failed code=" + e.statusCode)
        }
    }

}