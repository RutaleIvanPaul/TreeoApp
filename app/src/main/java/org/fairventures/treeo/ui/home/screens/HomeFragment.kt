package org.fairventures.treeo.ui.home.screens

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Context.CAMERA_SERVICE
import android.content.SharedPreferences
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.fairventures.treeo.R
import org.fairventures.treeo.adapters.HomeGuideListener
import org.fairventures.treeo.adapters.HomeGuideRecyclerAdapter
import org.fairventures.treeo.adapters.WhatsNewRecyclerAdapter
import org.fairventures.treeo.models.Activity
import org.fairventures.treeo.models.WhatsNew
import org.fairventures.treeo.ui.authentication.LoginLogoutViewModel
import org.fairventures.treeo.ui.home.HomeViewModel
import org.fairventures.treeo.util.DeviceInfoUtils
import org.fairventures.treeo.util.IDispatcherProvider
import org.fairventures.treeo.util.errors
import javax.inject.Inject


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment(), HomeGuideListener {

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
//        deleteUserDetailsFromSharePref()
    }


    override fun onStart() {
        super.onStart()
        getDeviceInformation()
    }

    override fun onResume() {
        super.onResume()
        getPlannedActivities()
//        homeViewModel.getNextTwoActivities()
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
        homeViewModel.nextTwoActivities.observe(viewLifecycleOwner, Observer { activities ->
            if (activities != null) {
                homeGuideRecyclerAdapter.submitList(activities)
            } else {
                Toast.makeText(context, errors.value, Toast.LENGTH_LONG).show()
            }
        })

        homeViewModel.plannedActivities.observe(viewLifecycleOwner, Observer { activities ->
            if (activities != null) {
                homeViewModel.insertActivity(activities)
            } else {
                Toast.makeText(context, errors.value, Toast.LENGTH_LONG).show()
            }
        })
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

    private fun getPlannedActivities() {
        homeViewModel.getPlannedActivities(getUserToken())
    }

    override fun onHomeGuideClick(activity: Activity) {
        findNavController().navigate(
            R.id.action_homeFragment_to_activityDetailsFragment,
            bundleOf("activity" to activity)
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}
