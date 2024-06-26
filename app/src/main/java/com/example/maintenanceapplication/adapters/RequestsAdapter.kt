package com.example.maintenanceapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.maintenanceapplication.R
import com.example.maintenanceapplication.databinding.RequestRecyclerItemBinding
import com.example.maintenanceapplication.model.Request


class RequestsAdapter(): RecyclerView.Adapter<RequestsAdapter.ViewHolder>() {
    private var requestList = ArrayList<Request>()
    var onItemClick : ((Request) -> Unit)? = null


    fun setRequestList(requestList: List<Request>){
        this.requestList = requestList as ArrayList<Request>
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: RequestRecyclerItemBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestsAdapter.ViewHolder {
       return ViewHolder(
           RequestRecyclerItemBinding.inflate(
               LayoutInflater.from(parent.context)
           )
       )
    }

    override fun onBindViewHolder(holder: RequestsAdapter.ViewHolder, position: Int) {

        holder.binding.problemName.text = requestList[position].name
        holder.binding.problemDateAndTime.text = requestList[position].date

        holder.binding.contentLayout.setOnClickListener {
            onItemClick!!.invoke(requestList[position])
        }

        if(requestList[position].status == "1"){
            holder.binding.problemStatus.setText(R.string.active)
            holder.binding.circle.background = ContextCompat.getDrawable(holder.itemView.context,R.drawable.circle_yellow)
        }
        if(requestList[position].status == "2"){
            holder.binding.problemStatus.setText(R.string.complete)
            holder.binding.circle.background = ContextCompat.getDrawable(holder.itemView.context,R.drawable.circle_green)
        }
        if(requestList[position].status == "3"){
            holder.binding.problemStatus.setText(R.string.cancel)
            holder.binding.circle.background = ContextCompat.getDrawable(holder.itemView.context,R.drawable.circle_red)
        }
    }
    override fun getItemCount(): Int {
        return  requestList.size
    }
}