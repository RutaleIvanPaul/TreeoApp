package org.fairventures.treeo.util

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import com.facebook.AccessToken
import com.facebook.Profile
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_choose_lang.*
import org.fairventures.treeo.R
import javax.inject.Inject



@AndroidEntryPoint
class ChooseLangFragment: Fragment() {
    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_choose_lang, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onRadioButtonClicked()
        onButtonClicked()
    }

    override fun onStart() {
        super.onStart()
        //Check if user has signed in before.
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())

        val accessToken: AccessToken? = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired

        val userToken = sharedPref.getString(getString(R.string.user_token), "")

        val mobileUsername = sharedPref.getString(getString(R.string.mobile_username), null)
        if (!sharedPref.getString(getString(R.string.loginManager), null).isNullOrEmpty()
            ||
                    !mobileUsername.isNullOrEmpty()
        ) {
            if (account != null) {
                Log.d("Sign In Details", account.idToken!!)
                openHome(account.displayName!!)
            } else if (isLoggedIn) {
                openHome(Profile.getCurrentProfile().name)
            } else if (!userToken.isNullOrBlank()) {
                openHome(userToken)
            }
            else{
                openHome(mobileUsername!!)
            }
        }
    }


    private fun onRadioButtonClicked(){
        val prefManager = PrefManager()
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.radio_english ->
                        prefManager.persist(requireActivity(), "en")
                    R.id.radio_luganda ->
                        prefManager.persist(requireActivity(), "lg")
                    R.id.radio_bahasa ->
                        prefManager.persist(requireActivity(), "in")
                    R.id.radio_spanish ->
                        prefManager.persist(requireActivity(), "es")
                }
            }
    }

    private fun onButtonClicked(){
        btn_continue.setOnClickListener {
            this.findNavController().navigate(
                R.id.action_chooseLangFragment_to_registrationFragment
            )
        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().recreate()
    }

    private fun openHome(extra: String) {
        this.findNavController().navigate(
            R.id.action_chooseLangFragment_to_homeFragment,
            bundleOf("username" to extra)
        )
    }
}