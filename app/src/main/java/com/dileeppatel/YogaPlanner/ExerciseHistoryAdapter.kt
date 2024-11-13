package com.dileeppatel.YogaPlanner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dileeppatel.YogaPlanner.databinding.RvHistoryItemViewBinding

class ExerciseHistoryAdapter(private val items : ArrayList<ExerciseEntity>,
                             private val deleteListener: (id:Int)->Unit
                             ): RecyclerView.Adapter<ExerciseHistoryAdapter.exerciseViewHolder>() {
    inner class exerciseViewHolder(val binding: RvHistoryItemViewBinding) :RecyclerView.ViewHolder(binding?.root) {
        val llContainer = binding.llContainer
        val tvIndex = binding.tvIndex
        val tvTitle = binding.tvTitle
        val tvTime = binding.tvTime
        val btnDelete = binding.btnDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): exerciseViewHolder {
        return exerciseViewHolder(RvHistoryItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: exerciseViewHolder, position: Int) {
        val context = holder.itemView.context
        val item = items[position]

        holder.tvIndex.text = position.plus(1).toString()
        holder.tvTitle.text = item.title
        holder.tvTime.text = item.time


        holder.btnDelete.setOnClickListener {
            deleteListener.invoke(item.id)
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }
}