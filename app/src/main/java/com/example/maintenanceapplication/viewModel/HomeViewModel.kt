package com.example.maintenanceapplication.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.maintenanceapplication.datasource.ServiceBuilder
import com.example.maintenanceapplication.interfaces.Api
import com.example.maintenanceapplication.model.ApiResponse
import com.example.maintenanceapplication.model.Request
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel: ViewModel() {
    private var requestListLiveData = MutableLiveData<List<Request>>()

    private var allRequestsListLiveData = MutableLiveData<List<Request>>()


    fun getRequestsByUserId(id: String,data:JsonObject){
        val api = ServiceBuilder.buildService(Api::class.java)
        api.findRequestByUserId(id,data).enqueue(object:Callback<ApiResponse<Request>>{
            override fun onResponse(
                call: Call<ApiResponse<Request>>,
                response: Response<ApiResponse<Request>>
            ) {
                 if(response.isSuccessful){
                     response.body()?.let { requestList->
                        requestListLiveData.postValue(requestList.result)
                     }
                 }
            }

            override fun onFailure(call: Call<ApiResponse<Request>>, t: Throwable) {
                Log.e("HomeViewModel",t.message.toString())
            }

        })
    }

    fun getAllRequests(){
        val api = ServiceBuilder.buildService(Api::class.java)
        api.getAllRequests().enqueue(object :Callback<ApiResponse<Request>>{
            override fun onResponse(
                call: Call<ApiResponse<Request>>,
                response: Response<ApiResponse<Request>>
            ) {
               if(response.isSuccessful){
                   response.body()?.let { requestList->
                       allRequestsListLiveData.postValue(requestList.result)
                   }
               }
            }

            override fun onFailure(call: Call<ApiResponse<Request>>, t: Throwable) {
                Log.e("HomeViewModel",t.message.toString())
            }

        })
    }

    fun observerRequestListLiveData(): LiveData<List<Request>>{
        return requestListLiveData
    }

    fun observerAllRequestsListLiveData(): LiveData<List<Request>>{
        return allRequestsListLiveData
    }


}