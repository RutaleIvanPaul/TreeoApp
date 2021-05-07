package org.fairventures.treeo.adapters

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.checkbox.MaterialCheckBox
import org.fairventures.treeo.R
import org.fairventures.treeo.db.models.Option
import javax.inject.Inject

private const val ITEM_VIEW_TYPE_CHECKBOX = 1
private const val ITEM_VIEW_TYPE_RADIO = 2

class QuestionnaireRecyclerAdapter(var selectedLanguage: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var list: List<Option>
    private lateinit var questionType: String
    private var lastSelectedPosition = -1

    companion object {
        val currentAnswers: MutableList<String> = mutableListOf()
    }

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
        val radioButton: RadioButton = itemView.findViewById(R.id.questionnaireOptionTextView)

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
                    holder.checkBox.text = list[position].option_title[selectedLanguage]
                    holder.checkBox.setOnClickListener {
                        manageAnswers(position)

                    }
                }
            }
            is RadioButtonViewHolder -> {
                holder.apply {
                    radioButton.text = list[position].option_title[selectedLanguage]
                    holder.radioButton.isChecked = lastSelectedPosition == position
                    radioButton.setOnClickListener {
                        manageRadioButtonAnswers(position)
                        lastSelectedPosition = adapterPosition;
                        notifyDataSetChanged();
                    }
                }
            }
        }
    }

    private fun manageAnswers(position: Int) {
        val option_code = list[position].option_code
        if (currentAnswers.contains(option_code)) {
            currentAnswers.remove(option_code)
        } else {
            currentAnswers.add(option_code)
        }
    }

    private fun manageRadioButtonAnswers(position: Int) {
        val option_code = list[position].option_code
        currentAnswers.clear()
        currentAnswers.add(option_code)
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
        currentAnswers.clear()
        notifyDataSetChanged()
    }

}