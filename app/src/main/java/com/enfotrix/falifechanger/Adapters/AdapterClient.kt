package com.enfotrix.falifechanger.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.enfotrix.falifechanger.Models.InvesterViewModel
import com.enfotrix.falifechanger.Models.TransactionModel
import com.enfotrix.falifechanger.Models.User
import com.enfotrix.falifechanger.R
import com.enfotrix.falifechanger.databinding.ItemUserBinding
import kotlinx.coroutines.launch



class AdapterClient(
    var activity: String,
    val userlist: List<User>,
    val listener: OnItemClickListener,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val viewModel: InvesterViewModel
) : RecyclerView.Adapter<AdapterClient.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(user: User)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(userlist[position])
    }

    override fun getItemCount(): Int {
        return userlist.size
    }

    inner class MyViewHolder(val itemBinding: ItemUserBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(user: User) {
            val context = itemBinding.root.context

            // Fetch and process investment data
            fetchAndProcessInvestmentData(user.id)

            itemBinding.tvInvestorName.text = user.firstName
            itemBinding.tvCNIC.text = user.cnic

            Glide.with(context)
                .load(user.photo)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(itemBinding.ivprofile)

            itemBinding.layUser.setOnClickListener {
                listener.onItemClick(user)
            }
        }

        private fun fetchAndProcessInvestmentData(userId: String) {
            var investment: Int = 0
            lifecycleScope.launch {
                viewModel.getInvestmentReq(userId)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val list = ArrayList<TransactionModel>()
                            if (task.result.size() > 0) {
                                for (document in task.result) {
                                    list.add(document.toObject(TransactionModel::class.java))
                                }
                                for (transaction in list.sortedByDescending { it.createdAt }) {
                                    if (transaction.newBalance.isNotEmpty()) {
                                        investment = transaction.newBalance.toInt()
                                        break
                                    }
                                }
                                itemBinding.tvinvestment.text = investment.toString()
                            }
                        }
                    }
            }
        }
    }
}