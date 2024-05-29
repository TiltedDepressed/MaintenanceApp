package com.example.maintenanceapplication.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.maintenanceapplication.databinding.ActivityChangeRequestDetailsBinding
import com.example.maintenanceapplication.datasource.ServiceBuilder
import com.example.maintenanceapplication.interfaces.Api
import com.example.maintenanceapplication.model.Request
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.util.Calendar

class ChangeRequestDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangeRequestDetailsBinding

    private lateinit var requestID: String

    private lateinit var sharePreference: SharedPreferences

    private lateinit var editor: SharedPreferences.Editor

    private lateinit var token: String

    private lateinit var myDate: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChangeRequestDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharePreference = this.getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)
        editor = sharePreference.edit()
        requestID = intent.getStringExtra("REQUEST_ID").toString()
        token = sharePreference.getString("TOKEN","").toString()

        binding.backButton.setOnClickListener {
            val intent = Intent(this@ChangeRequestDetailsActivity,RequestDetailActivity::class.java)
            intent.putExtra("REQUEST_ID",requestID)
            startActivity(intent)
        }

        getRequestData(requestID,token)

        binding.changeButton.setOnClickListener {
            if(binding.nameEditText.text.isNotEmpty() && binding.descriptionEditText.text.isNotEmpty()){
                val name = binding.nameEditText.text.toString()
                val description = binding.descriptionEditText.text.toString()
                updateRequestDetails(requestID,name,description,token,myDate)
            }
        }

        binding.deleteImageView.setOnClickListener {
            deleteRequest(requestID,token)
        }

    }

    private fun deleteRequest(requestID: String, token: String) {
        val data = JsonObject()
        data.addProperty("token",token)
        val api = ServiceBuilder.buildService(Api::class.java)
        api.deleteRequestByRequestId(requestID,data).enqueue(object:Callback<Request>{
            override fun onResponse(call: Call<Request>, response: Response<Request>) {
                if(response.isSuccessful){
                    val intent = Intent(this@ChangeRequestDetailsActivity,MainActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<Request>, t: Throwable) {
                Log.e("ChangeReqDetailsActivity",t.message.toString())
            }

        })
    }

    private fun updateRequestDetails(
        requestID: String,
        name: String,
        description: String,
        token: String,
        myDate: String
    ) {
        val data = JsonObject()
        data.addProperty("name",name)
        data.addProperty("description",description)
        data.addProperty("token",token)
        data.addProperty("date",myDate)
        val api = ServiceBuilder.buildService(Api::class.java)
        api.updateRequestByRequestId(requestID,data).enqueue(object:Callback<Request>{
            override fun onResponse(call: Call<Request>, response: Response<Request>) {
               if(response.isSuccessful){
                   val intent = Intent(this@ChangeRequestDetailsActivity,RequestDetailActivity::class.java)
                   intent.putExtra("REQUEST_ID",requestID)
                   startActivity(intent)
               }
            }

            override fun onFailure(call: Call<Request>, t: Throwable) {
                Log.e("ChangeReqDetailsActivity",t.message.toString())
            }

        })
    }

    private fun getRequestData(requestID: String, token: String) {
        val data = JsonObject()
        data.addProperty("token",token)
        val api = ServiceBuilder.buildService(Api::class.java)
        api.findRequestByRequestId(requestID,data).enqueue(object: Callback<Request> {
            override fun onResponse(call: Call<Request>, response: Response<Request>) {
                if(response.isSuccessful){
                    myDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)
                    binding.dateEditText.text = myDate
                    binding.nameEditText.setText(response.body()!!.name.toString())
                    binding.descriptionEditText.setText(response.body()!!.description.toString())
                }
            }
            override fun onFailure(call: Call<Request>, t: Throwable) {
                Log.e("ChangeReqDetailsActivity",t.message.toString())
            }

        })
    }
}