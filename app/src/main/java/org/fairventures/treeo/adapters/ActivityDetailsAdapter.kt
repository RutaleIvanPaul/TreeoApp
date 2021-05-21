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
import org.fairventures.treeo.db.models.Activity
import org.fairventures.treeo.models.ActivityDetail


class ActivityDetailsAdapter(
    private val context: Context,
    private val listener: HelperActivityListener
) :
    RecyclerView.Adapter<ActivityDetailsAdapter.ActivityDetailsHolder>() {
    //    var list: List<Activity> = listOf()
    var list: List<ActivityDetail> = listOf()

    //    var answerMap: Map<String, String?> = mapOf()
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
                listener.onActivityClick(list[position].activity)
            }

            val cardHeader = "Part " + (position + 1) + "- " + list[position].activity.title
            titleTextview.text = cardHeader
            val pages = list[position].activity.questionnaire!!.pages.toList()

            if (list[position].activity.is_complete) {
                editButton.visibility = View.VISIBLE
                startButton.visibility = View.GONE
                completeLayout.visibility = View.VISIBLE
                inCompleteLayout.visibility = View.GONE

                pages.forEach {
                    summaryContainer.addView(
                        inflateCompleteLayout(
                            it.header[language]!!,
                            list[position].questionAnswer[it.questionCode]
                        )
                    )
                }
            } else {
                pages.forEach {
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

    private fun inflateCompleteLayout(header: String, answer: String?): View {
        val inflater = context
            .applicationContext
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.page_summary_template_complete, null)
        val actionTextView: TextView = view.findViewById(R.id.activitySummaryCompleteTextview)
        val resultTextView: TextView = view.findViewById(R.id.activityAnswerCompleteTextview)
        actionTextView.text = header
        resultTextView.text = answer

        return view
    }

//    fun submitList(activities: List<Activity>, selectedLanguage: String) {
//        list = activities
//        language = selectedLanguage
//        notifyDataSetChanged()
//    }

    fun submitList(activities: List<ActivityDetail>, selectedLanguage: String) {
        list = activities
        language = selectedLanguage
        notifyDataSetChanged()
    }

//    fun submitAnswerMap(answers: Map<String, String?>) {
//        answerMap = answers
//        notifyDataSetChanged()
//    }

}

interface HelperActivityListener {
    fun onActivityClick(activity: Activity)
}


