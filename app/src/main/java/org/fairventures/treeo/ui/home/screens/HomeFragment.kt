package org.fairventures.treeo.ui.home.screens

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
import kotlinx.coroutines.*
import org.fairventures.treeo.R
import org.fairventures.treeo.adapters.HomeGuideRecyclerAdapter
import org.fairventures.treeo.adapters.WhatsNewRecyclerAdapter
import org.fairventures.treeo.db.models.*
import org.fairventures.treeo.models.ActivityTemplate
import org.fairventures.treeo.models.UserActivities
import org.fairventures.treeo.models.WhatsNew
import org.fairventures.treeo.ui.authentication.LoginLogoutViewModel
import org.fairventures.treeo.ui.home.HomeViewModel
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

    private lateinit var homeGuideRecyclerAdapter: HomeGuideRecyclerAdapter

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
    }



    override fun onStart() {
        super.onStart()
        getDeviceInformation()
    }

    override fun onResume() {
        super.onResume()
        getPlannedActivities()
        homeViewModel.getNextTwoActivities()
    }

    private fun setUpViews() {
        setUpWhatsNewRecycler()
        setUpHomeGuideRecycler()
    }

    private fun setUpHomeGuideRecycler() {
        homeGuideRecyclerAdapter = HomeGuideRecyclerAdapter(this)
        homeGuideRecycler.adapter = homeGuideRecyclerAdapter
        homeGuideRecycler.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun setUpWhatsNewRecycler() {
        whatsNewRecycler.adapter = WhatsNewRecyclerAdapter(requireContext(), getWhatsNewList())
        whatsNewRecycler.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
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
//                    deleteUserDetailsFromSharePref()
                    backToMain()
                } else {
                    Log.d("Logout", "Logout Response is null")
                }
            }
        )

//        homeViewModel.getAllActivities().observe(
//            viewLifecycleOwner,
//            Observer { activities ->
//                setUpHomeGuideRecycler(activities)
//            }
//        )

        homeViewModel.nextTwoActivities.observe(viewLifecycleOwner, Observer { activities ->
            homeGuideRecyclerAdapter.submitList(activities)
        })

        homeViewModel.plannedActivities.observe(
            viewLifecycleOwner,
            Observer {userActivities ->
                if (userActivities != null){
                    CoroutineScope(Dispatchers.IO).launch {
                        if(insertActivitiesFromApi(userActivities)){
                            homeViewModel.getNextTwoActivities()
                        }
                    }
                }
                else{
                    Log.d("Planned Activities", "Response is null")
                }
            }
        )

    }

    private suspend fun insertActivitiesFromApi(userActivities: UserActivities)  =
        CoroutineScope(Dispatchers.IO).async {
            val plannedActivityIds = mutableListOf<Long>()
            userActivities.plannedActivites.forEach { plannedActivity ->
                homeViewModel.insertActivity(
                        Activity(
                                type = plannedActivity.activityTemplate?.activityType!!,
                                due_date = plannedActivity.dueDate,
                                plot = plannedActivity.plot.toString(),
                                activity_id_from_remoteDB = plannedActivity.id,
                                activity_code = plannedActivity.activityTemplate.code.toString(),
                                questionnaire = Questionnaire(
                                        activity_id_from_remoteDB = plannedActivity.id,
                                        questionnaire_id_from_remote = plannedActivity.activityTemplate.questionnaire.id,
                                        questionnaire_title = plannedActivity.activityTemplate.questionnaire.configuration.title,
                                        pages = getPages(plannedActivity.activityTemplate.questionnaire.configuration.pages)
                                )
                        )
                )
                plannedActivityIds.add(plannedActivity.id)
            }
            return@async true
        }.await()


    private fun getPages(pages: List<org.fairventures.treeo.models.Page>): Array<Page> {
        val pagesList:MutableList<Page> = mutableListOf()
        pages.forEach{page ->
            pagesList.add(
                Page(
                    pageType = page.pageType,
                    questionCode = page.questionCode,
                    header = page.header,
                    description = page.description,
                    options = getOptions(page.options)
                )
            )
        }
        return pagesList.toTypedArray()
    }

    private fun getOptions(options: List<org.fairventures.treeo.models.Option>): Array<Option> {
        val optionsList = mutableListOf<Option>()
        Log.d("Options",options.toString())
        options.forEach {option ->
           optionsList.add(Option(
                option_title = option.title,
                option_code = option.code
            )
           )
        }
        return optionsList.toTypedArray()
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

    private fun getPlannedActivities(){
        homeViewModel.getPlannedActivities(getUserToken())
    }
}
