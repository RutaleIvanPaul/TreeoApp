package org.fairventures.treeo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.fairventures.treeo.R
import org.fairventures.treeo.db.models.Activity

class GuideRecyclerAdapter(private val listener: OnGuideClickListener) :
    RecyclerView.Adapter<GuideRecyclerAdapter.GuidePageViewHolder>() {

    var list = listOf<Activity>()

    class GuidePageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.guideObjectImageView)
        val dateTextView: TextView = itemView.findViewById(R.id.guideObjectDateTextView)
        val titleTextView: TextView = itemView.findViewById(R.id.guideObjectTitleTextView)
        val detailsTextView: TextView = itemView.findViewById(R.id.guideObjectDetailTextView)

        companion object {
            fun from(parent: ViewGroup): GuidePageViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.treeo_guide_object_layout, parent, false)
                return GuidePageViewHolder(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuidePageViewHolder {
        return GuidePageViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: GuidePageViewHolder, position: Int) {
        holder.apply {
//                Glide.with(context)
//                    .load(list[position].image)
//                    .into(imageView)
            dateTextView.text = list[position].due_date
            titleTextView.text = list[position].title
            detailsTextView.text = list[position].description
        }

        holder.itemView.setOnClickListener {
            listener.onClick(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun submitList(activities: List<Activity>) {
        list = activities
        notifyDataSetChanged()
    }
}

interface OnGuideClickListener {
    fun onClick(activity: Activity)
}
