package com.example.agentlifechanger.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agentlifechanger.Constants
import com.example.agentlifechanger.Models.User
import com.example.agentlifechanger.R
import com.example.agentlifechanger.UI.MainActivity
import com.example.agentlifechanger.databinding.ItemUserBinding

/*class AdapterClient(private val userlist : ArrayList<User>) : RecyclerView.Adapter<AdapterClient.MyViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterClient.MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_user,parent,false)

        return  MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: AdapterClient.MyViewHolder, position: Int) {
        val user : User = userlist[position]
        holder.name.text = user.firstName
        holder.cnic.text = user.cnic
    }

    override fun getItemCount(): Int {
        return userlist.size
    }


    public class  MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val name : TextView = itemView.findViewById(R.id.tvInvestorName)
        val cnic : TextView = itemView.findViewById(R.id.tvCNIC)



    }
}*/

class AdapterClient (var activity: String, val userlist: List<User>, val listener: OnItemClickListener)
    : RecyclerView.Adapter<AdapterClient.MyViewHolder>() {

    var constant= Constants()

    interface OnItemClickListener {
        fun onItemClick(user:User)
        /* fun onAssignClick(user:User)
         fun onRemoveClick(user:User)*/
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))

/*
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_user,parent,false)

        return  MyViewHolder(itemView)
*/
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(userlist[position])
        /*val user : User = userlist[position]
        holder.name.text = user.firstName
        holder.cnic.text = user.cnic*/
    }
    override fun getItemCount(): Int {
        return userlist.size
    }

    inner class  MyViewHolder(val itemBinding: ItemUserBinding) : RecyclerView.ViewHolder(itemBinding.root){

        fun bind(user:User) {
            val context = itemBinding.root.context

           /* if(activity.equals(constant.FROM_ASSIGNED_FA)) itemBinding.btnAssign.setVisibility(View.GONE)
            else if(activity.equals(constant.FROM_UN_ASSIGNED_FA)) itemBinding.btnRemove.setVisibility(View.GONE)
            else if(activity.equals(constant.FROM_PENDING_INVESTOR_REQ)) {
                itemBinding.btnRemove.setVisibility(View.GONE)
                itemBinding.btnAssign.setVisibility(View.GONE)
            }
*/
            itemBinding.tvInvestorName.text= "${user.firstName}"
            itemBinding.tvCNIC.text=user.cnic

            Glide.with(context)
                .load(user.photo)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background) // Placeholder image while loading
                .into(itemBinding.ivprofile)


            /*itemBinding.btnAssign.setOnClickListener { listener.onAssignClick(user) }
            itemBinding.btnRemove.setOnClickListener {
                listener.onRemoveClick(user)
            }*/
            itemBinding.layUser.setOnClickListener {

                listener.onItemClick(user) }


        }



    }



}
