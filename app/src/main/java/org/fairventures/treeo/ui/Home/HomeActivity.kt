package org.fairventures.treeo.ui.Home

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.app.Activity
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
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
import org.fairventures.treeo.util.DeviceInfoUtils
import org.fairventures.treeo.util.FILE_NAME
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPref: SharedPreferences

    @Inject
    lateinit var googleSignInOptions: GoogleSignInOptions

    private val loginLogoutUserViewModel: LoginLogoutUserViewModel by viewModels()

    private lateinit var photoFile: File


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        welcomeMessage.setText("Welcome ${intent.getStringExtra("username")}")

        val deviceInfoUtils = DeviceInfoUtils(applicationContext)

        loginLogoutUserViewModel.postDeviceData(
            deviceInfoUtils.getDeviceInformation(
            getSystemService(ACTIVITY_SERVICE) as ActivityManager,
            getSystemService(Context.SENSOR_SERVICE) as SensorManager,
            packageManager,
            getSystemService(CAMERA_SERVICE) as CameraManager
        ),
            getUserToken()
        )

        logout_button.setOnClickListener {
            logoutUser()
        }

        take_picture_button.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFile(FILE_NAME)

            val fileProvider = FileProvider
                .getUriForFile(this, "org.fairventures.treeo.fileprovider", photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            if(takePictureIntent.resolveActivity(this.packageManager) != null) {
                startActivityForResult(takePictureIntent, 1)

            }else{
                Log.d("Camera Error", "Could not open Camera")
            }
        }
    }

    private fun getPhotoFile(fileName: String): File {
        //acess package specific directories
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(fileName, ".jpg", storageDirectory)

    }

    fun saveToFile(bitmap:Bitmap) {
        val contextWrapper = ContextWrapper(applicationContext)
        val directory = contextWrapper.getDir("imageDir", Context.MODE_PRIVATE)
        val file = File(directory, "UniqueFileName" + ".jpg")
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
        }
        else{
            Log.d("path", "File Already Exists")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
            saveToFile(takenImage)
            imageView.setImageBitmap(takenImage)
        }else {
            super.onActivityResult(requestCode, resultCode, data)
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

    private fun getUserToken():String{
        with(sharedPref.edit()){
            val token = sharedPref.getString(getString(R.string.user_token), null)
            if (!token.isNullOrEmpty()) {
                return "Bearer $token"
            }
        }
        return ""
    }
}
