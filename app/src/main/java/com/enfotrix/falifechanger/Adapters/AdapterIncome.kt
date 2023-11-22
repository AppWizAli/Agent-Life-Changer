package com.enfotrix.falifechanger.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.falifechanger.Constants
import com.enfotrix.falifechanger.Models.AgentTransactionModel
import com.enfotrix.falifechanger.databinding.ItemIncomeBinding
import java.text.SimpleDateFormat
import java.util.Locale

class AdapterIncome ( val data: List<AgentTransactionModel>) : RecyclerView.Adapter<AdapterIncome.ViewHolder>(){


    var constant= Constants()

    interface OnItemClickListener {
        fun onItemClick(transactionModel: AgentTransactionModel)
        fun onDeleteClick(transactionModel: AgentTransactionModel)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemIncomeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) { holder.bind(data[position]) }
    override fun getItemCount(): Int { return data.size }
    inner class ViewHolder(val itemBinding: ItemIncomeBinding) : RecyclerView.ViewHolder(itemBinding.root){

        fun bind(transactionModel: AgentTransactionModel) {


            val dateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
            val formattedDate = transactionModel.transactionAt?.toDate()?.let { dateFormat.format(it) }
           itemBinding.tvtransactionAt.text =
               transactionModel.transactionAt?.toDate()
                   ?.let { SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(it) }
            itemBinding.tvIncome.text = transactionModel.salary
            itemBinding.tvRemarks.text = transactionModel.remarks


        }

    }

}