package org.fairventures.treeo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.fairventures.treeo.R
import org.fairventures.treeo.models.Activity
import java.text.SimpleDateFormat
import java.util.*

class HomeGuideRecyclerAdapter(
    private val listener: HomeGuideListener
) :
    RecyclerView.Adapter<HomeGuideRecyclerAdapter.GuideViewHolder>() {

    var list: List<Activity> = listOf()

    class GuideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.guideObjectImageView)
        val dateTextView: TextView = itemView.findViewById(R.id.guideObjectDateTextView)
        val titleTextView: TextView = itemView.findViewById(R.id.guideObjectTitleTextView)
        val detailsTextView: TextView = itemView.findViewById(R.id.guideObjectDetailTextView)

        companion object {
            fun from(parent: ViewGroup): GuideViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.treeo_guide_object_layout, parent, false)
                return GuideViewHolder(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
        return GuideViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
        holder.apply {
//            Glide.with(context)
//                .load(list[position].dueDate)
//                .into(imageView)
//            dateTextView.text = convertLongToTime(list[position].dueDate)
            titleTextView.text = list[position].title
            detailsTextView.text = list[position].description
        }

        holder.itemView.setOnClickListener {
            listener.onHomeGuideClick(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun convertLongToTime(time: String): String {
        val simpleDateFormat = SimpleDateFormat("dd.LLL.yyyy", Locale.getDefault())
        return simpleDateFormat.format(time).toString()
    }

    fun submitList(newList: List<Activity>) {
        list = newList
        notifyDataSetChanged()
    }
}

interface HomeGuideListener {
    fun onHomeGuideClick(activity: Activity)
}

