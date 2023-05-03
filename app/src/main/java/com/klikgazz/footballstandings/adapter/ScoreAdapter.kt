package com.klikgazz.footballstandings.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.klikgazz.footballstandings.R
import com.klikgazz.footballstandings.databinding.ItemScoreBinding
import com.klikgazz.footballstandings.model.ResultModel

class ScoreAdapter : RecyclerView.Adapter<ScoreAdapter.ViewHolder>() {

    var itemList = mutableListOf<String>()
    var dataScore = HashMap<String, ResultModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_score, parent, false))

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount() : Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemScoreBinding.bind(itemView)
        fun bind() {
            binding.apply {
                val textWatcher = object : TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        if (etClubNameHome.text.isNullOrBlank()){
                            etClubNameHome.error = "Wajib di isi"
                        }

                        if (etClubNameAway.text.isNullOrBlank()){
                            etClubNameAway.error = "Wajib di isi"
                        }

                        if (etScoreHome.text.isNullOrBlank()){
                            etScoreHome.error = "Wajib di isi"
                        }

                        if (etScoreAway.text.isNullOrBlank()){
                            etScoreAway.error = "Wajib di isi"
                        }
                    }

                    override fun afterTextChanged(p0: Editable?) {
                        val data = ResultModel(etClubNameHome.text.toString().lowercase(), etScoreHome.text.toString(), etClubNameAway.text.toString().lowercase(), etScoreAway.text.toString())
                        dataScore[adapterPosition.toString()] = data
                    }
                }
                etClubNameHome.addTextChangedListener(textWatcher)
                etClubNameAway.addTextChangedListener(textWatcher)
                etScoreAway.addTextChangedListener(textWatcher)
                etScoreHome.addTextChangedListener(textWatcher)
            }
        }
    }
}