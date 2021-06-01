package org.fairventures.treeo.ui.questionnaire

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_request_camera_use.*
import kotlinx.android.synthetic.main.fragment_request_camera_use.view.*
import org.fairventures.treeo.R

class DisplayPhotoFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_display_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        view.toolbar.inflateMenu(R.menu.main_menu)
        view.toolbar.setNavigationOnClickListener {
            view.findNavController()
                .navigate(R.id.action_displayPhotoFragment_to_takeLandPhotos2)
        }
         initializeButton()
    }

    private fun initializeButton(){
        btn.setOnClickListener {
            view?.findNavController()
                ?.navigate(R.id.action_displayPhotoFragment_to_takeLandPhotos2)
        }
    }
}
