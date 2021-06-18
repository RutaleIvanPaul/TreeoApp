package org.fairventures.treeo.ui.questionnaire

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_request_camera_use.view.*
import kotlinx.android.synthetic.main.fragment_take_land_photos.*
import org.fairventures.treeo.R

class TakeSoilPhotoFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_take_soil_photos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        view.toolbar.inflateMenu(R.menu.main_menu)
        view.toolbar.setNavigationOnClickListener {
            view.findNavController()
                .navigate(R.id.action_takeSoilPhotoFragment_to_soilPhotosFragment)
        }
        setUpFab()
    }

    private fun setUpFab() {
        take_picture_button.setOnClickListener {
            view?.findNavController()
                ?.navigate(R.id.cameraFragment)
        }
    }
}
