package org.fairventures.treeo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.fairventures.treeo.R
import org.fairventures.treeo.models.ActivitySummaryItem
import org.fairventures.treeo.models.Option


class ActivitySummaryAdapter(
    private val context: Context,
    private val summaryListener: ActivitySummaryListener
) : RecyclerView.Adapter<ActivitySummaryAdapter.ActivityDetailsHolder>() {
    var list: List<ActivitySummaryItem> = listOf()
    var language = ""

    class ActivityDetailsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val summaryContainer: LinearLayout =
            itemView.findViewById(R.id.activitySummaryContainer)
        val inCompleteLayout: LinearLayout =
            itemView.findViewById(R.id.durationInCompleteLinearLayout)
        val completeLayout: LinearLayout =
            itemView.findViewById(R.id.durationCompletedLinearLayout)
        val titleTextview: TextView =
            itemView.findViewById(R.id.activityDetailsTitleTextview)
        val startButton: Button =
            itemView.findViewById(R.id.activityDetailsStartButton)
        val editButton: Button =
            itemView.findViewById(R.id.activityDetailsEditButton)

        companion object {
            fun from(parent: ViewGroup): ActivityDetailsHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(
                    R.layout.activity_details_item_questionnaire,
                    parent,
                    false
                )
                return ActivityDetailsHolder(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityDetailsHolder {
        return ActivityDetailsHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ActivityDetailsHolder, position: Int) {
        holder.apply {
            itemView.setOnClickListener {
                summaryListener.onActivityClick(list[position])
            }
            startButton.setOnClickListener {
                summaryListener.onActivityClick(list[position])
            }
            editButton.setOnClickListener {
                summaryListener.onActivityClick(list[position])
            }

            val cardHeader = "Part " + (position + 1) + "- " + list[position].activity.title
            titleTextview.text = cardHeader

            if (list[position].activity.isCompleted) {
                editButton.visibility = View.VISIBLE
                startButton.visibility = View.GONE
                completeLayout.visibility = View.VISIBLE
                inCompleteLayout.visibility = View.GONE

                clearContainer(summaryContainer)
                list[position].pages.forEach {
                    summaryContainer.addView(
                        inflateCompleteLayout(
                            it.header[language]!!,
                            it.options!!
                        )
                    )
                }
            } else {
                clearContainer(summaryContainer)
                list[position].pages.forEach {
                    summaryContainer.addView(
                        inflateIncompleteLayout(
                            it.header[language]!!,
                            context.resources.getString(R.string.placeholder_dashes)
                        )
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun inflateIncompleteLayout(header: String, answer: String): View {
        val inflater = context
            .applicationContext
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.page_summary_template_incomplete, null)
        val actionTextView: TextView = view.findViewById(R.id.activitySummaryIncompleteTextview)
        val resultTextView: TextView = view.findViewById(R.id.activityAnswerIncompleteTextview)
        actionTextView.text = header
        resultTextView.text = answer

        return view
    }

    private fun inflateCompleteLayout(header: String, options: List<Option>): View {
        val inflater = context
            .applicationContext
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.page_summary_template_complete, null)
        val actionTextView: TextView = view.findViewById(R.id.activitySummaryCompleteTextview)
        val resultTextView: TextView = view.findViewById(R.id.activityAnswerCompleteTextview)
        actionTextView.text = header
        options.forEach {
            if (it.isSelected) {
                resultTextView.text = it.title[language]
            }
        }
        return view
    }

    private fun clearContainer(view: LinearLayout) {
        if (view.childCount > 0) {
            view.removeAllViews()
        }
    }

    fun submitList(activities: List<ActivitySummaryItem>, selectedLanguage: String) {
        list = activities
        language = selectedLanguage
        notifyDataSetChanged()
    }
}

interface ActivitySummaryListener {
    fun onActivityClick(activity: ActivitySummaryItem)
}

