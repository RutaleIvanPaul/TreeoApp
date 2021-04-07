package org.fairventures.treeo.ui.home

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Context.CAMERA_SERVICE
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.fairventures.treeo.R
import org.fairventures.treeo.ui.authentication.LoginLogoutUserViewModel
import org.fairventures.treeo.util.DeviceInfoUtils
import org.fairventures.treeo.util.ExifUtil
import org.fairventures.treeo.util.FILE_NAME
import org.fairventures.treeo.util.IDispatcherProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment() {

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
        welcomeMessage.text = arguments?.getString("username")

        getDeviceInformation()

        logout_button.setOnClickListener {
            logoutUser()
        }

        take_picture_button.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFile(FILE_NAME)

            val fileProvider = FileProvider
                .getUriForFile(requireContext(), "org.fairventures.treeo.fileprovider", photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivityForResult(takePictureIntent, 1)

            } else {
                Log.d("Camera Error", "Could not open Camera")
            }
        }

        setObservers()
        simulateCrash()
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

    private fun getPhotoFile(fileName: String): File {
        //acess package specific directories
        val storageDirectory =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    private fun saveToFile(bitmap: Bitmap) {
        val contextWrapper = ContextWrapper(requireActivity().applicationContext)
        val directory = contextWrapper.getDir("imageDir", Context.MODE_PRIVATE)
        val file = File(directory, System.currentTimeMillis().toString() + ".jpg")
        if (!file.exists()) {
            Log.d("path", file.toString())
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.flush()
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            Log.d("path", "File Already Exists")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
            saveToFile(takenImage)
            val rotatedImage = ExifUtil.rotateBitmap(photoFile.absolutePath, takenImage)
            imageView.setImageBitmap(rotatedImage)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
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
            if (!token.isNullOrEmpty()) {
                loginLogoutUserViewModel.logout(token)
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
    }
}
