package com.trustledger.adminaitrust.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trustledger.adminaitrust.models.PlanModel
import com.trustledger.adminaitrust.databinding.ItemInvestmentPlansBinding

class PlansByCategoryAdapter(var list : List<PlanModel>,val clickHandler: ClickHandler ) : RecyclerView.Adapter<PlansByCategoryAdapter.Holder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Holder {
        return Holder(ItemInvestmentPlansBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: Holder,
        position: Int
    ) {
        val plans = list[position]
        holder.binding.packageName.text = plans.planName
        holder.binding.planAmount.text = plans.minAmount.toString() + "$"
        holder.binding.planDays.text = plans.planDays.toString()
        holder.binding.planDailyPercentage.text = plans.dailyPercentage.toString() + "%"
        holder.binding.planDirectProfit.text = plans.directProfit.toString() + "%"
        holder.binding.edit.setOnClickListener {
            clickHandler.onClick(plans)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(newList : List<PlanModel>){
        list = newList
        notifyDataSetChanged()
    }

    inner class Holder(val binding : ItemInvestmentPlansBinding) : RecyclerView.ViewHolder(binding.root)

    interface ClickHandler{
        fun onClick(planModel: PlanModel)
    }
}