package org.fairventures.treeo.ui.questionnaire

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_request_camera_use.view.*
import kotlinx.android.synthetic.main.fragment_soil_photos.*
import org.fairventures.treeo.R

class SoilPhotosFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_soil_photos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        view.toolbar.inflateMenu(R.menu.main_menu)

        setupUI()
    }

    private fun setupUI() {
        soilPhotoStart.setOnClickListener {
            view?.findNavController()
                ?.navigate(
                    R.id.action_soilPhotosFragment_to_takeLandPhotos2,
                    bundleOf("soilPhoto" to true)
                )
        }
    }

}
