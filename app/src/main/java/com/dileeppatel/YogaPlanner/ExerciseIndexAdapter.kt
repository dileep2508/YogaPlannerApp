package com.dileeppatel.YogaPlanner

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dileeppatel.YogaPlanner.databinding.RvExerciseIndexItemBinding
import java.lang.Integer.parseInt

class ExerciseIndexAdapter(
    private val items: ArrayList<ExerciseModel>,
    private val selectListener: (exerciseNumber: Int) -> Unit
): RecyclerView.Adapter<ExerciseIndexAdapter.viewHolder>() {
    inner class viewHolder(val itemBinding: RvExerciseIndexItemBinding):RecyclerView.ViewHolder(itemBinding.root) {
        fun bindItem(item:ExerciseModel){
            itemBinding.tvExerciseIndex.text = item.getId().toString()
            if(item.getIsSelected()){
                itemBinding.tvExerciseIndex.setBackgroundResource(R.drawable.rv_normal_item_current)
            }else if(item.getIsCompleted()){
                itemBinding.tvExerciseIndex.setBackgroundResource(R.drawable.rv_normal_item_completed)
            }else{
                itemBinding.tvExerciseIndex.setBackgroundResource(R.drawable.rv_normal_item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(RvExerciseIndexItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.bindItem(items[position])
        holder.itemBinding.tvExerciseIndex.setOnClickListener {
            selectListener.invoke( parseInt((it as TextView).text.toString()) )
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
