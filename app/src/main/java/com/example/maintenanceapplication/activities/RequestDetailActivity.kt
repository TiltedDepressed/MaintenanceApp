package com.example.maintenanceapplication.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.maintenanceapplication.R
import com.example.maintenanceapplication.databinding.ActivityRequestDetailBinding
import com.example.maintenanceapplication.datasource.ServiceBuilder
import com.example.maintenanceapplication.interfaces.Api
import com.example.maintenanceapplication.model.Favorites
import com.example.maintenanceapplication.model.Request
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RequestDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRequestDetailBinding

    private lateinit var requestID: String

    private lateinit var sharePreference: SharedPreferences

    private lateinit var editor: SharedPreferences.Editor

    private lateinit var token: String

    private lateinit var status: String

    private lateinit var userRole: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRequestDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharePreference = this.getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)
        editor = sharePreference.edit()
        requestID = intent.getStringExtra("REQUEST_ID").toString()
        token = sharePreference.getString("TOKEN","").toString()
        userRole = sharePreference.getString("ROLE","").toString()

        getRequestData(requestID,token)

        if (userRole == "1"){

            binding.settingsImageView.visibility = View.INVISIBLE

        }

        if (userRole == "2"){
            binding.settingsImageView.visibility = View.VISIBLE
            binding.settingsImageView.setOnClickListener {
                val intent = Intent(this@RequestDetailActivity,ChangeStatusActivity::class.java)
                intent.putExtra("REQUEST_ID",requestID)
                startActivity(intent)
            }

        }



        binding.changeButton.setOnClickListener {
            val intent = Intent(this@RequestDetailActivity,ChangeRequestDetailsActivity::class.java)
            intent.putExtra("REQUEST_ID",requestID)
            startActivity(intent)
        }


        binding.backButton.setOnClickListener {
            val intent = Intent(this@RequestDetailActivity,MainActivity::class.java)
            startActivity(intent)
        }

        binding.favoriteImageView.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val date =  binding.dateEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()
            val userId = sharePreference.getString("USER_ID","").toString()
            addToFavorites(requestID,name,date,description,userId,token,status)
        }

    }

    private fun addToFavorites(
        requestID: String,
        name: String,
        date: String,
        description: String,
        userId: String,
        token: String,
        status: String
    ) {
        val data = JsonObject()
        data.addProperty("userId",userId)
        data.addProperty("requestId",requestID)
        data.addProperty("name",name)
        data.addProperty("description",description)
        data.addProperty("date",date)
        data.addProperty("token",token)
        data.addProperty("status",status)
        val api = ServiceBuilder.buildService(Api::class.java)
        api.createNewFavorites(data).enqueue(object:Callback<Favorites>{
            override fun onResponse(call: Call<Favorites>, response: Response<Favorites>) {
                if(response.isSuccessful){
                    Toast.makeText(applicationContext, "Added", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Favorites>, t: Throwable) {
                Log.e("RequestDetailActivity",t.message.toString())
            }

        })

    }

    private fun getRequestData(requestID: String, token: String) {
        val data = JsonObject()
        data.addProperty("token",token)
        val api = ServiceBuilder.buildService(Api::class.java)
        api.findRequestByRequestId(requestID,data).enqueue(object:Callback<Request>{
            override fun onResponse(call: Call<Request>, response: Response<Request>) {
               if(response.isSuccessful){
                   binding.dateEditText.text = response.body()!!.date.toString()
                   binding.nameEditText.text = response.body()!!.name.toString()
                   binding.descriptionEditText.text = response.body()!!.description.toString()
                   status = response.body()!!.status.toString()
               }
            }
            override fun onFailure(call: Call<Request>, t: Throwable) {
                Log.e("RequestDetailActivity",t.message.toString())
            }

        })
    }
}