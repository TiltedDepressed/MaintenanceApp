package com.example.maintenanceapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
        holder.binding.problemStatus.text = requestList[position].status
    }
    override fun getItemCount(): Int {
        return  requestList.size
    }
}