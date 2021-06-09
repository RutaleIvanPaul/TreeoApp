package org.fairventures.treeo.ui.questionnaire

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_display_photo.*
import kotlinx.android.synthetic.main.fragment_request_camera_use.view.*
import org.fairventures.treeo.R
import org.fairventures.treeo.db.models.LandSurvey
import org.fairventures.treeo.util.ExifUtil

class DisplayPhotoFragment: Fragment() {

    private val takeLandPhotosViewModel: TakeLandPhotosViewModel by activityViewModels()
    private var cornersTaken = 1
    private var sequenceNumber = 0

    private var soilPhoto = false

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
        cornersTaken = arguments?.getInt("cornersTaken")!!
        sequenceNumber = arguments?.getInt("sequenceNumber")!!

        if(arguments?.getBoolean("soilPhoto")?:soilPhoto){
            soilPhoto = true
        }
         setupUI()
    }

    private fun setupUI(){
        val bitmap = BitmapFactory.decodeFile(arguments?.getString("imagePath")!!)
        val rotatedBitmap = ExifUtil.rotateBitmap(arguments?.getString("imagePath")!!,bitmap)
        imageViewClear.setImageBitmap(rotatedBitmap)
        title_text_clear_photo.text = arguments?.getString("title_text")

        if(soilPhoto){
            btn_continue.text = "Finish Land Specification"
        }
        btn_retake.setOnClickListener {
            view?.findNavController()
                ?.navigate(
                    R.id.action_displayPhotoFragment_to_takeLandPhotos2,
                    bundleOf(
                        "cornersTaken" to arguments?.getInt("cornersTaken"),
                        "numberOfCorners" to arguments?.getInt("numberOfCorners"),
                        "sequenceNumber" to ++sequenceNumber,
                        "soilPhoto" to arguments?.getBoolean("soilPhoto")
                    )
                )
        }

        btn_continue.setOnClickListener {
            takeLandPhotosViewModel.insertLandSurveyItem(
                LandSurvey(
                    null,
                    arguments?.getInt("sequenceNumber")!!,
                    arguments?.getString("imagePath")!!,
                    if(soilPhoto) "soilPhoto" else "landPhoto"
                )
            )

            if (arguments?.getBoolean("landPhotosDone?")!!){
                view?.findNavController()
                    ?.navigate(R.id.action_displayPhotoFragment_to_soilPhotosFragment)
            }
            else if(soilPhoto){
                view?.findNavController()
                    ?.navigate(R.id.action_displayPhotoFragment_to_landSpecificationFragment)
            }
            else{
                view?.findNavController()
                    ?.navigate(
                        R.id.action_displayPhotoFragment_to_takeLandPhotos2,
                        bundleOf(
                            "cornersTaken" to ++cornersTaken,
                            "numberOfCorners" to arguments?.getInt("numberOfCorners"),
                            "sequenceNumber" to ++sequenceNumber
                        )
                    )
            }
        }
    }
}
