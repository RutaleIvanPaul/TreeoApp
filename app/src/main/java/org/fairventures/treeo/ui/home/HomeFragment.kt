package org.fairventures.treeo.ui.home

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Context.CAMERA_SERVICE
import android.content.SharedPreferences
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.fairventures.treeo.R
import org.fairventures.treeo.adapters.WhatsNewRecyclerAdapter
import org.fairventures.treeo.db.models.Activity
import org.fairventures.treeo.db.models.Option
import org.fairventures.treeo.db.models.Page
import org.fairventures.treeo.db.models.Questionnaire
import org.fairventures.treeo.models.WhatsNew
import org.fairventures.treeo.ui.authentication.LoginLogoutViewModel
import org.fairventures.treeo.util.DeviceInfoUtils
import org.fairventures.treeo.util.IDispatcherProvider
import javax.inject.Inject

//typealias LumaListener = (luma: Double) -> Unit

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var sharedPref: SharedPreferences

    @Inject
    lateinit var deviceInfoUtils: DeviceInfoUtils

    @Inject
    lateinit var dispatcher: IDispatcherProvider

    private val loginLogoutViewModel: LoginLogoutViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseCrashlytics.getInstance().setUserId(getUserId().toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setObservers()
        insertActivity()
    }

    private fun insertActivity() {
        val activities = arrayOf(
                Activity(
            type = " ",
            due_date = " ",
            plot = null,
            activity_id_from_remoteDB = 2,
            acitivity_code = "land",
            questionnaire = Questionnaire(
                activity_id_from_remoteDB = 2,
                questionnaire_id_from_remote = 3,
                questionnaire_title = mapOf("en" to "title", "lg" to "taito"),
                pages = arrayOf(
                    Page(
                        pageType = "checkbox",
                        questionCode = "qc",
                        header = mapOf("en" to "this header", "lg" to "omutwe"),
                        description = mapOf("en" to "description", "lg" to "desc"),
                        options = arrayOf(
                            Option(
                                option_title = mapOf("en" to "this option", "lg" to "oputioni"),
                                option_code = "oc"
                            )
                        )
                    )
                )
            )
        ),
                Activity(
                        type = " ",
                        due_date = " ",
                        plot = null,
                        activity_id_from_remoteDB = 3,
                        acitivity_code = "land",
                        questionnaire = Questionnaire(
                                activity_id_from_remoteDB = 3,
                                questionnaire_id_from_remote = 3,
                                questionnaire_title = mapOf("en" to "title", "lg" to "taito"),
                                pages = arrayOf(
                                        Page(
                                                pageType = "checkbox",
                                                questionCode = "qc",
                                                header = mapOf("en" to "this header", "lg" to "omutwe"),
                                                description = mapOf("en" to "description", "lg" to "desc"),
                                                options = arrayOf(
                                                        Option(
                                                                option_title = mapOf("en" to "this option", "lg" to "oputioni"),
                                                                option_code = "oc"
                                                        )
                                                )
                                        )
                                )
                        )
                ),
                Activity(
                        type = " ",
                        due_date = " ",
                        plot = null,
                        activity_id_from_remoteDB = 2,
                        acitivity_code = "land",
                        questionnaire = Questionnaire(
                                activity_id_from_remoteDB = 2,
                                questionnaire_id_from_remote = 3,
                                questionnaire_title = mapOf("en" to "title", "lg" to "taito"),
                                pages = arrayOf(
                                        Page(
                                                pageType = "checkbox",
                                                questionCode = "qc",
                                                header = mapOf("en" to "this", "lg" to "omutwe"),
                                                description = mapOf("en" to "description", "lg" to "desc"),
                                                options = arrayOf(
                                                        Option(
                                                                option_title = mapOf("en" to "this option", "lg" to "oputioni"),
                                                                option_code = "oc"
                                                        )
                                                )
                                        )
                                )
                        )
                )
        )
        activities.forEach {activity ->
            homeViewModel.insertActivity(activity)
        }
    }

    override fun onStart() {
        super.onStart()
        getDeviceInformation()
    }

    private fun setUpViews() {
        setUpWhatsNewRecycler()
    }

    private fun setUpWhatsNewRecycler() {
        whatsNewRecycler.adapter = WhatsNewRecyclerAdapter(requireContext(), getWhatsNewList())
        whatsNewRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun getWhatsNewList(): List<WhatsNew> {
        return listOf(
            WhatsNew(
                R.drawable.trees_1,
                "What the fern?",
                resources.getString(R.string.lorem_ipsum)
            ),
            WhatsNew(
                R.drawable.trees_2,
                "Get started with Baobab",
                resources.getString(R.string.lorem_ipsum)
            ),
            WhatsNew(
                R.drawable.trees_3,
                "Understanding Eucalyptus",
                resources.getString(R.string.lorem_ipsum)
            )
        )
    }

    private fun setObservers() {
        loginLogoutViewModel.logoutResponse.observe(
            viewLifecycleOwner,
            Observer { logoutResponse ->
                if (logoutResponse != null) {
                    deleteUserDetailsFromSharePref()
                    backToMain()
                } else {
                    Log.d("Logout", "Logout Response is null")
                }
            }
        )

        homeViewModel.getAllActivities().observe(
            viewLifecycleOwner,
            Observer {activities ->
                Log.d("DBActivities", activities[0].toString())
            }
        )
    }

    private fun getDeviceInformation() {
        GlobalScope.launch(dispatcher.main()) {
            loginLogoutViewModel.postDeviceData(
                deviceInfoUtils.getDeviceInformation(
                    requireActivity().getSystemService(ACTIVITY_SERVICE) as ActivityManager,
                    requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager,
                    requireActivity().packageManager,
                    requireActivity().getSystemService(CAMERA_SERVICE) as CameraManager
                ),
                getUserToken()
            )
        }
    }

    private fun logoutUser() {
        val loginManager = sharedPref.getString(getString(R.string.loginManager), "")
        when {
            loginManager.equals(getString(R.string.google)) -> {
            }
            loginManager.equals(getString(R.string.facebook)) -> {
                logoutFromBackend()
            }
            else -> {
                logoutFromBackend()
            }
        }
    }

    private fun backToMain() {
//        this.findNavController()
//            .navigate(R.id.action_homeFragment_to_registrationFragment)
    }

    private fun logoutFromBackend() {
        with(sharedPref.edit()) {
            val token = sharedPref.getString(getString(R.string.user_token), null)
            val mobile_username = sharedPref.getString(getString(R.string.mobile_username), null)
            if (!token.isNullOrEmpty()) {
                loginLogoutViewModel.logout(token)
            } else if (!mobile_username.isNullOrEmpty()) {
                with(sharedPref.edit()) {
                    remove(getString(R.string.mobile_username))
                    apply()
                }
                backToMain()
            }
        }
    }

    private fun getUserToken(): String {
        with(sharedPref.edit()) {
            val token = sharedPref.getString(getString(R.string.user_token), null)
            if (!token.isNullOrEmpty()) {
                return "Bearer $token"
            }
            apply()
        }
        return ""
    }

    private fun deleteUserDetailsFromSharePref() {
        with(sharedPref.edit()) {
            remove(getString(R.string.user_token))
            remove(getString(R.string.loginManager))
            apply()
        }
    }

    private fun getUserId(): Int {
        with(sharedPref.edit()) {
            val id =
                sharedPref.getInt(resources.getString(org.fairventures.treeo.R.string.user_id), 0)
            if (id != 0) {
                return id
            }
            apply()
        }
        return 0
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}
