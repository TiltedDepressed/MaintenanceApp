package com.example.maintenanceapplication.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.maintenanceapplication.R
import com.example.maintenanceapplication.databinding.ActivityChangeStatusBinding
import com.example.maintenanceapplication.datasource.ServiceBuilder
import com.example.maintenanceapplication.interfaces.Api
import com.example.maintenanceapplication.model.Request
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangeStatusActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangeStatusBinding

    private lateinit var requestID: String

    private lateinit var sharePreference: SharedPreferences

    private lateinit var editor: SharedPreferences.Editor

    private lateinit var token: String

    private lateinit var status : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChangeStatusBinding.inflate(layoutInflater)
        sharePreference = this.getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)
        editor = sharePreference.edit()
        requestID = intent.getStringExtra("REQUEST_ID").toString()
        token = sharePreference.getString("TOKEN","").toString()
        setContentView(binding.root)
        
        binding.firstRadioButton.setOnClickListener {
             status = "1"
         updateRequestStatus(requestID,token,status)
        }

        binding.secondRadioButton.setOnClickListener {
            status = "2"
            updateRequestStatus(requestID,token,status)
        }
        binding.thirdRadioButton.setOnClickListener {
            status = "3"
            updateRequestStatus(requestID,token,status)
        }

        binding.backButton.setOnClickListener {
            val intent = Intent(this@ChangeStatusActivity,RequestDetailActivity::class.java)
            intent.putExtra("REQUEST_ID",requestID)
            startActivity(intent)
        }
    }

    private fun updateRequestStatus(requestID: String, token: String, status: String) {
        val data = JsonObject()
        data.addProperty("token",token)
        data.addProperty("status",status)
        val api = ServiceBuilder.buildService(Api::class.java)
        api.updateRequestByRequestId(requestID,data).enqueue(object:Callback<Request>{
            override fun onResponse(call: Call<Request>, response: Response<Request>) {
               if(response.isSuccessful){
                   val intent = Intent(this@ChangeStatusActivity,MainActivity::class.java)
                   startActivity(intent)
               }
            }

            override fun onFailure(call: Call<Request>, t: Throwable) {

            }

        })
    }
}