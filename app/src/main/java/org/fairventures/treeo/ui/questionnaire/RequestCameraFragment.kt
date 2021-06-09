package org.fairventures.treeo.ui.questionnaire

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import com.vmadalin.easypermissions.models.PermissionRequest
import kotlinx.android.synthetic.main.fragment_request_camera_use.*
import kotlinx.android.synthetic.main.fragment_request_camera_use.view.*
import org.fairventures.treeo.R

class RequestCameraFragment: Fragment(), EasyPermissions.PermissionCallbacks {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_request_camera_use, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        view.toolbar.inflateMenu(R.menu.main_menu)
        view.toolbar.setNavigationOnClickListener {
            view.findNavController()
                .navigate(
                    R.id.action_requestCameraFragment_to_activitySummaryFragment,
                    bundleOf("activity" to arguments?.getParcelable("activity"))
                )
        }
        initializeButton()
    }

    private fun initializeButton(){
        btn_turn_on_camera.setOnClickListener {
            checkAndRequestCameraPermissions()
        }
    }

    private fun checkAndRequestCameraPermissions() {
        if (allPermissionsGranted()) {
            goToNextFragment()
        } else {
            if (isOSVersionMorHigher()) {
                EasyPermissions.requestPermissions(
                    this,
                    PermissionRequest.Builder(requireContext())
                        .code(RequestCameraFragment.REQUEST_CODE_PERMISSIONS)
                        .perms(RequestCameraFragment.REQUIRED_PERMISSIONS)
                        .rationale("Please Grant Permission to use your Camera")
                        .build()
                )
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    RequestCameraFragment.REQUIRED_PERMISSIONS,
                    RequestCameraFragment.REQUEST_CODE_PERMISSIONS
                )
            }
        }
    }

    private fun goToNextFragment() {
        view?.findNavController()
            ?.navigate(
                R.id.action_requestCameraFragment_to_landSurveyPrepFragment
            )
    }

    private fun allPermissionsGranted(): Boolean {
        if (isOSVersionMorHigher()) {
            RequestCameraFragment.REQUIRED_PERMISSIONS.forEach {
                if (!EasyPermissions.hasPermissions(requireContext(), it)) {
                    return false
                }
            }
        } else {
            RequestCameraFragment.REQUIRED_PERMISSIONS.forEach {
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

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        Toast.makeText(requireContext(), "Permissions Not Granted", Toast.LENGTH_LONG).show()
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        RequestCameraFragment.REQUIRED_PERMISSIONS.forEach {
            if (EasyPermissions.somePermissionPermanentlyDenied(this, it)) {
                SettingsDialog.Builder(requireContext()).build().show()
            }
        }
    }


    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        goToNextFragment()
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = RequestCameraFragment()

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