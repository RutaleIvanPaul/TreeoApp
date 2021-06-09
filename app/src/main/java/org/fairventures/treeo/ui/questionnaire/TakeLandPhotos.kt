package org.fairventures.treeo.ui.questionnaire

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
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
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.gms.location.*
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import com.vmadalin.easypermissions.models.PermissionRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_request_camera_use.view.*
import kotlinx.android.synthetic.main.fragment_take_land_photos.*
import kotlinx.android.synthetic.main.fragment_take_land_photos.take_picture_button
import org.fairventures.treeo.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class TakeLandPhotos: Fragment(), EasyPermissions.PermissionCallbacks, SensorEventListener {

    @Inject
    lateinit var sharedPref: SharedPreferences

    private var locationManager: LocationManager? = null
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var outputDirectory: File

    var sensorManager: SensorManager? = null

    private var picture_taken_before: Boolean = false
    private var currentSteps: Float = 0f
    private var previousSteps: Float =0f

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var cornersTaken = 1

    private var numberOfCorners = 1

    private var imagePath = ""

    private var sequenceNumber = 0

    private var soilPhoto = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_take_land_photos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        view.toolbar.inflateMenu(R.menu.main_menu)
        view.toolbar.setNavigationOnClickListener {
            view.findNavController()
                .navigate(R.id.action_takeLandPhotos2_to_landCornersFragment)
        }

        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager

        numberOfCorners = arguments?.getInt("numberOfCorners")!!

        if (arguments?.getInt("cornersTaken") != 0){
            cornersTaken = arguments?.getInt("cornersTaken")?:cornersTaken
        }

        if(arguments?.getInt("sequenceNumber")!=0){
            sequenceNumber = arguments?.getInt("sequenceNumber")?:sequenceNumber
        }

        if(arguments?.getBoolean("soilPhoto")?:soilPhoto){
            soilPhoto = true
            setUpUI2()
        }
        else{
            setUpUI(numberOfCorners)
        }
        checkAndRequestCameraPermissions()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()

        var stepsSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepsSensor == null) {
            Toast.makeText(requireContext(), "No Step Counter Sensor !", Toast.LENGTH_LONG).show()
            TakeLandPhotos.REQUIRED_PERMISSIONS = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            sensorManager?.registerListener(this, stepsSensor, SensorManager.SENSOR_STATUS_ACCURACY_HIGH)
        }
    }

    private fun setUpUI(numberOfCorners: Int?) {
        take_picture_button.setOnClickListener {
            takePhoto()
        }
        title_text.text = "Land Photo ${cornersTaken}/${numberOfCorners}"
    }

    private fun setUpUI2() {
        title_text.text = "Soil Photo 1/1"

        take_picture_button.setOnClickListener {
            takePhoto()
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
                    it.setSurfaceProvider(landSoilPhoto.createSurfaceProvider())
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
                    this, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                Log.e(TakeLandPhotos.TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext())
        )
    }

    private fun takePhoto() {
        if (allPermissionsGranted()) {
            if (isLocationEnabled(locationManager!!)) {
                // Get a stable reference of the modifiable image capture use case
                val imageCapture = imageCapture ?: return

                // Create time-stamped output file to hold the image
                val photoFile = File(
                    outputDirectory,
                    getUserId().toString() + ".jpg"
                )

                // Create output options object which contains file + metadata
                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()


                // Set up image capture listener, which is triggered after photo has
                // been taken
                imageCapture.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(requireContext()),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onError(exc: ImageCaptureException) {
                            Log.e(TakeLandPhotos.TAG, "Photo capture failed: ${exc.message}", exc)
                        }

                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            val savedUri = Uri.fromFile(photoFile)
                            val msg = "Photo capture succeeded: $savedUri"
                            Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                            Log.d(TakeLandPhotos.TAG, msg)

                            imagePath = photoFile.absolutePath

                            if (!picture_taken_before){
                                picture_taken_before = true
                                previousSteps = currentSteps
                            }

                            getMetadataOfSavedImage(photoFile)

                            navigateToDisplayPhoto()
                        }
                    })
            } else {
                checkAndRequestLocation()
            }
        } else {
            checkAndRequestCameraPermissions()
        }
    }

    private fun navigateToDisplayPhoto() {
        if(soilPhoto){
            view?.findNavController()
                ?.navigate(
                    R.id.action_takeLandPhotos2_to_displayPhotoFragment2,
                    bundleOf(
                        "soilPhoto" to soilPhoto,
                        "title_text" to title_text.text.toString(),
                        "imagePath" to imagePath
                    )
                )
        }
        else if (cornersTaken == numberOfCorners) {
            view?.findNavController()
                ?.navigate(
                    R.id.action_takeLandPhotos2_to_displayPhotoFragment2,
                    bundleOf(
                        "landPhotosDone?" to true,
                        "cornersTaken" to cornersTaken,
                        "numberOfCorners" to numberOfCorners,
                        "imagePath" to imagePath,
                        "title_text" to title_text.text.toString(),
                        "sequenceNumber" to sequenceNumber
                    )
                )
        } else {
            view?.findNavController()
                ?.navigate(
                    R.id.action_takeLandPhotos2_to_displayPhotoFragment2,
                    bundleOf(
                        "landPhotosDone?" to false,
                        "cornersTaken" to cornersTaken,
                        "numberOfCorners" to numberOfCorners,
                        "imagePath" to imagePath,
                        "title_text" to title_text.text.toString(),
                        "sequenceNumber" to sequenceNumber
                    )
                )
        }
    }

    private fun allPermissionsGranted(): Boolean {
        if (isOSVersionMorHigher()) {
            TakeLandPhotos.REQUIRED_PERMISSIONS.forEach {
                if (!EasyPermissions.hasPermissions(requireContext(), it)) {
                    return false
                }
            }
        } else {
            TakeLandPhotos.REQUIRED_PERMISSIONS.forEach {
                if (ContextCompat.checkSelfPermission(
                        requireContext(), it
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }

        return true
    }

    private fun isOSVersionMorHigher(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    private fun isLocationEnabled(locationManager: LocationManager): Boolean {
        return locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun getOutputDirectory(): File {
        val contextWrapper = ContextWrapper(requireActivity().applicationContext)
        val directory = contextWrapper.getDir("imageDir", Context.MODE_PRIVATE)
        val file = File(
            directory, SimpleDateFormat(
                TakeLandPhotos.FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis())
        ).apply { mkdir() }

        return file
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

    private fun getMetadataOfSavedImage(photofile: File) {

        Log.d("Exif",(currentSteps-previousSteps).toString())

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

    fun getLastLocation() {
        if (allPermissionsGranted()) {

            if (isLocationEnabled(locationManager!!)) {
                try {
                    fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                        var location: Location? = task.result
                        if (location == null) {
                            newLocationData()
                        } else {
                            Log.d(
                                "Exif Location",
                                "You Current Location is : Long: ${location.longitude} , Lat: ${location.latitude}, Accuracy ${location.accuracy} meters"
                            )
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
                } catch (e: SecurityException) {
                    Log.d("Exif Exception", e.message.toString())
                }
            } else {
                Log.d("Exif Location", "Location not turned on.")
                Toast.makeText(
                    requireContext(),
                    "Please Turn on Your device Location",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    fun newLocationData() {
        var locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
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
            locationRequest, locationCallback, Looper.myLooper()
        )
    }


    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
        }
    }

    private fun checkAndRequestLocation() {
        Toast.makeText(requireContext(), "Please Turn on Location first", Toast.LENGTH_LONG).show()
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
                        .code(TakeLandPhotos.REQUEST_CODE_PERMISSIONS)
                        .perms(TakeLandPhotos.REQUIRED_PERMISSIONS)
                        .rationale("Please Grant Permission to use your Camera")
                        .build()
                )
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    TakeLandPhotos.REQUIRED_PERMISSIONS,
                    TakeLandPhotos.REQUEST_CODE_PERMISSIONS
                )
            }
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        Toast.makeText(requireContext(), "Permissions Not Granted", Toast.LENGTH_LONG).show()
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        TakeLandPhotos.REQUIRED_PERMISSIONS.forEach {
            if (EasyPermissions.somePermissionPermanentlyDenied(this, it)) {
                SettingsDialog.Builder(requireContext()).build().show()
            }
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(requireContext(), "Permissions Granted", Toast.LENGTH_LONG).show()
        startCamera()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        currentSteps = event?.values?.get(0)!!
        Log.d("Steps:" , event?.values?.get(0).toString())

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = TakeLandPhotos()

        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private var REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACTIVITY_RECOGNITION
        )
    }
}
