package com.klikgazz.footballstandings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.klikgazz.footballstandings.R
import com.klikgazz.footballstandings.databinding.ItemClubBinding
import com.klikgazz.footballstandings.model.ClubsModel

class TableAdapter : RecyclerView.Adapter<TableAdapter.ViewHolder>() {

    private var listData = emptyList<ClubsModel>()
    private var originalList = emptyList<ClubsModel>()

    fun setData(newListData: List<ClubsModel>?) {
        if (newListData == null) return
        val diffUtil = MyDiffUtil(listData, newListData)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        listData = newListData
        originalList = newListData
        diffResults.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_club, parent, false))

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemClubBinding.bind(itemView)
        fun bind(data: ClubsModel) {
            binding.apply {
                val position = adapterPosition + 1
                tvNo.text = position.toString()
                tvClub.text = data.name.toString()
                tvDraw.text = data.draw.toString()
                tvLose.text = data.lose.toString()
                tvMatch.text = data.match.toString()
                tvGoalLose.text = data.goalLose.toString()
                tvGoalWin.text = data.goalWin.toString()
                tvWin.text = data.win.toString()
                tvPoint.text = data.point.toString()
            }
        }
    }

    class MyDiffUtil(
        private val oldList: List<ClubsModel>,
        private val newList: List<ClubsModel>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].name == newList[newItemPosition].name
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}