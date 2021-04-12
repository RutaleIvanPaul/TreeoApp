package org.fairventures.treeo.ui.measure

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
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
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.DEFAULT_SETTINGS_REQ_CODE
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import com.vmadalin.easypermissions.models.PermissionRequest
import kotlinx.android.synthetic.main.fragment_camera.*
import org.fairventures.treeo.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

class CameraFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    @Inject
    lateinit var sharedPref: SharedPreferences

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    private var imageCapture: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Request camera permissions
        checkAndRequestCameraPermissions()
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
                        .code(CameraFragment.REQUEST_CODE_PERMISSIONS)
                        .perms(arrayOf(CameraFragment.REQUIRED_PERMISSIONS))
                        .rationale("Please Grant Permission to use your Camera")
                        .build()
                )
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(CameraFragment.REQUIRED_PERMISSIONS),
                    CameraFragment.REQUEST_CODE_PERMISSIONS
                )
            }
        }
    }

    private fun takePhoto() {
        if (allPermissionsGranted()) {
            // Get a stable reference of the modifiable image capture use case
            val imageCapture = imageCapture ?: return

            // Create time-stamped output file to hold the image
            val photoFile = File(
                outputDirectory,
                getUserId().toString() + ".jpg"
            )

            // Create output options object which contains file + metadata
            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

            // Set up image capture listener, which is triggered after photo has been taken
            imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(requireContext()),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
                        Log.e(CameraFragment.TAG, "Photo capture failed: ${exc.message}", exc)
                    }

                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        val savedUri = Uri.fromFile(photoFile)
                        val msg = "Photo capture succeeded: $savedUri"
                        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                        Log.d(CameraFragment.TAG, msg)
                    }
                })
        } else {
            checkAndRequestCameraPermissions()
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
                    this, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                Log.e(CameraFragment.TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun getOutputDirectory(): File {
        val contextWrapper = ContextWrapper(requireActivity().applicationContext)
        val directory = contextWrapper.getDir("imageDir", Context.MODE_PRIVATE)
        val file = File(
            directory, SimpleDateFormat(
                CameraFragment.FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis())
        ).apply { mkdir() }

        return file
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DEFAULT_SETTINGS_REQ_CODE) {
            if (allPermissionsGranted()) {
                Toast.makeText(requireContext(), "Permissions Granted", Toast.LENGTH_LONG).show()
                startCamera()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CameraFragment.REQUEST_CODE_PERMISSIONS) {
            if (isOSVersionMorHigher()) {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
                EasyPermissions.onRequestPermissionsResult(
                    requestCode,
                    permissions,
                    grantResults,
                    this
                )
            } else {
                if (allPermissionsGranted()) {
                    Toast.makeText(requireContext(), "Permissions Granted", Toast.LENGTH_LONG)
                        .show()
                    startCamera()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Permissions not granted by the user.",
                        Toast.LENGTH_SHORT
                    ).show()
                    requireActivity().finish()
                }
            }
        }
    }

    private fun allPermissionsGranted(): Boolean {
        if (isOSVersionMorHigher()) {
            return EasyPermissions.hasPermissions(
                requireContext(),
                CameraFragment.REQUIRED_PERMISSIONS
            )
        } else {
            return ContextCompat.checkSelfPermission(
                requireContext(), CameraFragment.REQUIRED_PERMISSIONS
            ) == PackageManager.PERMISSION_GRANTED

        }
    }

    private fun isOSVersionMorHigher(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        Toast.makeText(requireContext(), "Permissions Not Granted", Toast.LENGTH_LONG).show()
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(
                this,
                CameraFragment.REQUIRED_PERMISSIONS
            )
        ) {
            SettingsDialog.Builder(requireContext()).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(requireContext(), "Permissions Granted", Toast.LENGTH_LONG).show()
        startCamera()
    }

    private fun getUserId(): Int {
        with(sharedPref.edit()) {
            val id =
                sharedPref.getInt(resources.getString(R.string.user_id), 0)
            if (id != 0) {
                return id
            }
            apply()
        }
        return 0
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = CameraFragment()

        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = Manifest.permission.CAMERA
    }
}
