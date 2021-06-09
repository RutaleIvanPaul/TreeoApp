package org.fairventures.treeo.ui.questionnaire

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_land_corners.*
import kotlinx.android.synthetic.main.fragment_request_camera_use.view.*
import org.fairventures.treeo.R
import org.fairventures.treeo.util.enableView

class LandCornersFragment: Fragment() {
    private var numberOfCorners = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_land_corners, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        view.toolbar.inflateMenu(R.menu.main_menu)
        view.toolbar.setNavigationOnClickListener {
            view.findNavController()
                .navigate(R.id.action_landCornersFragment_to_landPhotosFragment)
        }

        initializeUI()
    }

    private fun initializeUI(){
        questionnaireOptionTextView3.setOnClickListener { radioButtonChecked(questionnaireOptionTextView3) }
        questionnaireOptionTextView4.setOnClickListener { radioButtonChecked(questionnaireOptionTextView4) }
        questionnaireOptionTextView5.setOnClickListener { radioButtonChecked(questionnaireOptionTextView5) }
        questionnaireOptionTextView6.setOnClickListener { radioButtonChecked(questionnaireOptionTextView6) }
        questionnaireOptionTextView6andmore.setOnClickListener { radioButtonChecked(questionnaireOptionTextView6andmore) }

        corners_btn_continue.setOnClickListener {
            if (numberOfCorners == 0){
                Toast.makeText(requireContext(),"First Pick a choice",Toast.LENGTH_LONG).show()
            }
            else {
                view?.findNavController()
                    ?.navigate(
                        R.id.action_landCornersFragment_to_takeLandPhotos2,
                        bundleOf("numberOfCorners" to numberOfCorners)
                    )
            }
        }
    }

    private fun radioButtonChecked(questionnaireOptionTextView: RadioButton?) {
        val radioButtons = listOf<RadioButton>(
            questionnaireOptionTextView3,questionnaireOptionTextView4,
            questionnaireOptionTextView5,questionnaireOptionTextView6,
            questionnaireOptionTextView6andmore
        )
        radioButtons.forEach{radioButton ->
            if (radioButton.isChecked && radioButton!=questionnaireOptionTextView){
                radioButton.isChecked = false
            }
        }
        enableView(corners_btn_continue)
        numberOfCorners = Integer.valueOf(questionnaireOptionTextView?.text.toString().split(" ")[0])

    }
}
