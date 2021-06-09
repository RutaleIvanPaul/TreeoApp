package org.fairventures.treeo.ui.questionnaire

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_land_specification.*
import kotlinx.android.synthetic.main.fragment_request_camera_use.view.*
import org.fairventures.treeo.R


class LandSpecificationFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_land_specification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        view.toolbar.inflateMenu(R.menu.main_menu)
        view.toolbar.setNavigationOnClickListener {
            view.findNavController()
                .navigate(R.id.action_landSpecificationFragment_to_displaySoilPhotoFragment)
        }
        initializeButton()
        setUpUploadStatusView()

    }

    private fun initializeButton(){
        btn_continue.setOnClickListener {
            view?.findNavController()
                ?.navigate(R.id.action_landSpecificationFragment_to_homeFragment)
        }
    }

    private fun setUpUploadStatusView(){
        val uploadStatusView: View = layoutInflater
            .inflate(R.layout.upload_status, linearlayout, false)
        linearlayout.addView(uploadStatusView)
    }
}