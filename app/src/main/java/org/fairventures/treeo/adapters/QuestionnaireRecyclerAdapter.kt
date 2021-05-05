package org.fairventures.treeo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textview.MaterialTextView
import org.fairventures.treeo.R
import org.fairventures.treeo.db.models.Option

private const val ITEM_VIEW_TYPE_CHECKBOX = 1
private const val ITEM_VIEW_TYPE_RADIO = 2

class QuestionnaireRecyclerAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var list: List<Option>
    private lateinit var questionType: String

    class CheckBoxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: MaterialCheckBox = itemView.findViewById(R.id.questionnaireCheckBox)

        companion object {
            fun from(parent: ViewGroup): CheckBoxViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.question_checkbox_item, parent, false)
                return CheckBoxViewHolder(view)
            }
        }
    }

    class RadioButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val radioButton: MaterialTextView = itemView.findViewById(R.id.questionnaireOptionTextView)

        companion object {
            fun from(parent: ViewGroup): RadioButtonViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.question_radio_item, parent, false)
                return RadioButtonViewHolder(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_CHECKBOX -> CheckBoxViewHolder.from(parent)
            ITEM_VIEW_TYPE_RADIO -> RadioButtonViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CheckBoxViewHolder -> {
                holder.apply {
                    holder.checkBox.text = list[position].option_title["en"]
                    itemView.setOnClickListener {

                    }
                }
            }
            is RadioButtonViewHolder -> {
                holder.apply {
                    holder.radioButton.text = list[position].option_title["en"]
                    itemView.setOnClickListener {

                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (questionType) {
            "checkbox" -> {
                ITEM_VIEW_TYPE_CHECKBOX
            }
            "radio" -> {
                ITEM_VIEW_TYPE_RADIO
            }
            else -> 0
        }
    }

    fun submitList(options: List<Option>, flag: String) {
        list = options
        questionType = flag
        notifyDataSetChanged()
    }
}