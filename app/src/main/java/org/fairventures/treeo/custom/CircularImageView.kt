package org.fairventures.treeo.custom

import android.content.Context
import android.util.AttributeSet
import android.view.ViewOutlineProvider
import androidx.appcompat.widget.AppCompatImageView
import org.fairventures.treeo.R

class CircularImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    init {
        //the outline (view edges) of the view should be derived    from the background
        outlineProvider = ViewOutlineProvider.BACKGROUND

        //cut the view to match the view to the outline of the background
        clipToOutline = true

        //use the following background to calculate the outline
        setBackgroundResource(R.drawable.circular_background)

        //fill in the whole image view, crop if needed while keeping the center
        scaleType = ScaleType.CENTER_CROP
    }
}
