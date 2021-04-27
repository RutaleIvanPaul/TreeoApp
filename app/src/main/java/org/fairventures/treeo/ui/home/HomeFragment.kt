package org.fairventures.treeo.ui.home

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Context.*
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.location.Location
import android.location.LocationManager
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.location.*
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.DEFAULT_SETTINGS_REQ_CODE
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import com.vmadalin.easypermissions.models.PermissionRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.fairventures.treeo.R
import org.fairventures.treeo.ui.authentication.LoginLogoutUserViewModel
import org.fairventures.treeo.util.DeviceInfoUtils
import org.fairventures.treeo.util.IDispatcherProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment(), EasyPermissions.PermissionCallbacks  {

    @Inject
    lateinit var sharedPref: SharedPreferences

    @Inject
    lateinit var googleSignInOptions: GoogleSignInOptions


    @Inject
    lateinit var deviceInfoUtils: DeviceInfoUtils

    @Inject
    lateinit var dispatcher: IDispatcherProvider

    private val loginLogoutUserViewModel: LoginLogoutUserViewModel by viewModels()

    private lateinit var photoFile: File

    private var imageCapture: ImageCapture? = null

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    private var locationManager : LocationManager? = null
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseCrashlytics.getInstance().setUserId(getUserId().toString())

        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        locationManager = requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager


    }

    private fun checkAndRequestCameraPermissions() {
        if (allPermissionsGranted()) {
            Toast.makeText(requireContext(), "Permissions Granted", Toast.LENGTH_LONG).show()
            startCamera()
        } else {
            if (isOSVersionMorHigher()) {
                EasyPermissions.requestPermissions(
                    this,
                    PermissionRequest.Builder(requireContext())
                        .code(REQUEST_CODE_PERMISSIONS)
                        .perms(REQUIRED_PERMISSIONS)
                        .rationale("Please Grant Permission to use your Camera")
                        .build()
                )
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
                )
            }
        }
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
        // Request camera permissions
        checkAndRequestCameraPermissions()

        welcomeMessage.text = arguments?.getString("username")

        getDeviceInformation()

        setupUI()

        setObservers()
        simulateCrash()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DEFAULT_SETTINGS_REQ_CODE) {
            if (allPermissionsGranted() && isLocationEnabled(locationManager!!)){
                Toast.makeText(requireContext(), "Permissions Granted", Toast.LENGTH_LONG).show()
                startCamera()
            }

        }
    }

    private fun setupUI() {
        logout_button.setOnClickListener {
            logoutUser()
        }

        take_picture_button.setOnClickListener {
            takePhoto()
        }
    }

    private fun takePhoto() {
        if(allPermissionsGranted()) {
            if (isLocationEnabled(locationManager!!)) {
                // Get a stable reference of the modifiable image capture use case
                val imageCapture = imageCapture ?: return

                // Create time-stamped output file to hold the image
                val photoFile = File(
                        outputDirectory,
                        getUserId().toString() + ".jpg")

                // Create output options object which contains file + metadata
                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                // Set up image capture listener, which is triggered after photo has
                // been taken
                imageCapture.takePicture(
                        outputOptions, ContextCompat.getMainExecutor(requireContext()), object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
                        Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                    }

                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        val savedUri = Uri.fromFile(photoFile)
                        val msg = "Photo capture succeeded: $savedUri"
                        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                        Log.d(TAG, msg)

                        getMetadataOfSavedImage(photoFile)
                    }
                })
            }
            else{
                checkAndRequestLocation()
            }
        }
        else{
            checkAndRequestCameraPermissions()
        }
    }

    private fun checkAndRequestLocation() {
        Toast.makeText(requireContext(),"Please Turn on Location first",Toast.LENGTH_LONG).show()
    }

    private fun getMetadataOfSavedImage(photofile: File) {
        val exifInterface = ExifInterface(photofile.absolutePath)
        val tagsToCheck = arrayOf(
                ExifInterface.TAG_DATETIME,
                ExifInterface.TAG_FLASH,
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.TAG_IMAGE_WIDTH,
                ExifInterface.TAG_IMAGE_LENGTH
        )


        getLastLocation()

        //Incase we need to keep constant track of locations

//        try {
//            // Request location updates
//            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, locationListener)
//        } catch(ex: SecurityException) {
//            Log.d("EXIF ATTRIBUTES", "Security Exception, no location available")
//        }

        for (tag in tagsToCheck) {
            exifInterface.getAttribute(tag)?.also {
                Log.d("EXIF ATTRIBUTES", it)
            }
        }
    }

    private fun isLocationEnabled(locationManager: LocationManager):Boolean{
        return locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun getLastLocation(){
        if (allPermissionsGranted()) {

            if (isLocationEnabled(locationManager!!)) {
                try {
                    fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                        var location: Location? = task.result
                        if (location == null) {
                            newLocationData()
                        } else {
                            Log.d("Exif Location", "You Current Location is : Long: ${location.longitude} , Lat: ${location.latitude}, Accuracy ${location.accuracy} meters")
                            val result = FloatArray(1)
                            Location.distanceBetween(
                                    location.latitude,
                                    location.longitude,
                                    0.3121138,
                                    32.5859096,
                                    result
                            )
                            Log.d("Exif Distance", "${result[0]}meters")
                        }
                    }
                }
                catch(e:SecurityException){
                    Log.d("Exif Exception",e.message.toString())
                }
            } else {
                Log.d("Exif Location","Location not turned on.")
                Toast.makeText(requireContext(), "Please Turn on Your device Location", Toast.LENGTH_LONG).show()
            }
        }
    }


    fun newLocationData(){
        var locationRequest =  LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        if (
                ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
                !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
                !=
                PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        fusedLocationProviderClient!!.requestLocationUpdates(
                locationRequest,locationCallback, Looper.myLooper()
        )
    }


    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
        }
    }

    private fun startCamera() {
        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.createSurfaceProvider())
                }

            imageCapture = ImageCapture.Builder()
                .build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun getOutputDirectory(): File {
        val contextWrapper = ContextWrapper(requireActivity().applicationContext)
        val directory = contextWrapper.getDir("imageDir", Context.MODE_PRIVATE)
        val file = File(directory, SimpleDateFormat(FILENAME_FORMAT, Locale.US
        ).format(System.currentTimeMillis())).apply { mkdir() }

        return file
    }

    private fun setObservers() {
        loginLogoutUserViewModel.logoutResponse.observe(
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
    }

    private fun getDeviceInformation() {
        GlobalScope.launch(dispatcher.main()) {
            loginLogoutUserViewModel.postDeviceData(
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


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if(isOSVersionMorHigher()){
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
                EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
            }
            else{
                if (allPermissionsGranted()) {
                    Toast.makeText(requireContext(),"Permissions Granted",Toast.LENGTH_LONG).show()
                    startCamera()
                } else {
                    Toast.makeText(requireContext(),
                        "Permissions not granted by the user.",
                        Toast.LENGTH_SHORT).show()
                    requireActivity().finish()
                }
            }
        }
    }

    private fun allPermissionsGranted(): Boolean{
        if (isOSVersionMorHigher()){
            REQUIRED_PERMISSIONS.forEach {
                if (!EasyPermissions.hasPermissions(requireContext(), it)){
                    return false
                }
            }
        }else {
            REQUIRED_PERMISSIONS.forEach {
                if(ContextCompat.checkSelfPermission(
                                requireContext(), it
                        ) != PackageManager.PERMISSION_GRANTED){
                    return false
                }
            }
        }

        return true
    }

    private fun isOSVersionMorHigher():Boolean{
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }


    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        Toast.makeText(requireContext(), "Permissions Not Granted", Toast.LENGTH_LONG).show()
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        REQUIRED_PERMISSIONS.forEach {
            if (EasyPermissions.somePermissionPermanentlyDenied(this, it)) {
                SettingsDialog.Builder(requireContext()).build().show()
            }
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(requireContext(), "Permissions Granted", Toast.LENGTH_LONG).show()
        startCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun logoutUser() {
        val loginManager = sharedPref.getString(getString(R.string.loginManager), "")
        when {
            loginManager.equals(getString(R.string.google)) -> {
                signOutGoogle()
            }
            loginManager.equals(getString(R.string.facebook)) -> {
                LoginManager.getInstance().logOut()
                logoutFromBackend()
            }
            else -> {
                logoutFromBackend()
            }
        }
    }

    private fun backToMain() {
        this.findNavController()
            .navigate(R.id.action_homeFragment_to_registrationFragment)
    }

    private fun logoutFromBackend() {
        with(sharedPref.edit()) {
            val token = sharedPref.getString(getString(R.string.user_token), null)
            val mobile_username = sharedPref.getString(getString(R.string.mobile_username), null)
            if (!token.isNullOrEmpty()) {
                loginLogoutUserViewModel.logout(token)
            }
            else if(!mobile_username.isNullOrEmpty()){
                with(sharedPref.edit()) {
                    remove(getString(R.string.mobile_username))
                    apply()
                }
                backToMain()
            }
        }
    }

    private fun signOutGoogle() {
        val mGoogleSignInClient =
            GoogleSignIn.getClient(requireActivity(), googleSignInOptions)
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(requireActivity()) {
                logoutFromBackend()
            }
    }

    private fun getUserToken(): String {
        with(sharedPref.edit()) {
            val token = sharedPref.getString(getString(R.string.user_token), null)
            if (!token.isNullOrEmpty()) {
                return "Bearer $token"
            }
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

    private fun simulateCrash() {
        crash_button.setOnClickListener {
            throw RuntimeException("Test Crash")
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()

        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    //define the listener for incase we need to keep track of locations
//    private val locationListener: LocationListener = object : LocationListener {
//        override fun onLocationChanged(location: Location) {
//            Log.d ("EXIF LOcation","" + location.longitude + ":" + location.latitude)
//        }
//        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
//        override fun onProviderEnabled(provider: String) {}
//        override fun onProviderDisabled(provider: String) {}
//    }
}
