package com.example.maintenanceapplication.viewModel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.maintenanceapplication.datasource.ServiceBuilder
import com.example.maintenanceapplication.interfaces.Api
import com.example.maintenanceapplication.model.ApiResponse
import com.example.maintenanceapplication.model.Favorites
import com.example.maintenanceapplication.model.Request
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryViewModel: ViewModel() {

    private lateinit var sharePreference: SharedPreferences

    private lateinit var editor: SharedPreferences.Editor


    private var requestListLiveData = MutableLiveData<List<Favorites>>()

    fun favoritesByUserId(id: String, data:JsonObject){
        val api = ServiceBuilder.buildService(Api::class.java)
        api.findFavoritesByUserId(id,data).enqueue(object:Callback<ApiResponse<Favorites>>{
            override fun onResponse(
                call: Call<ApiResponse<Favorites>>,
                response: Response<ApiResponse<Favorites>>
            ) {
                if(response.isSuccessful){
                    response.body()?.let { requestList->
                        requestListLiveData.postValue(requestList.result)
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse<Favorites>>, t: Throwable) {
                Log.e("HistoryViewModel",t.message.toString())
            }

        })
    }

    fun observerRequestListLiveData(): LiveData<List<Favorites>>{
        return requestListLiveData
    }

}