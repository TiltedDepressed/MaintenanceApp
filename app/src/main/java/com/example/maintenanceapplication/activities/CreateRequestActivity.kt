package com.example.maintenanceapplication.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.maintenanceapplication.databinding.ActivityCreateRequestBinding
import com.example.maintenanceapplication.datasource.ServiceBuilder
import com.example.maintenanceapplication.interfaces.Api
import com.example.maintenanceapplication.model.Request
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.util.Calendar
import java.util.Date

class CreateRequestActivity : AppCompatActivity() {



    private lateinit var binding: ActivityCreateRequestBinding

    private lateinit var sharePreference: SharedPreferences

    private lateinit var editor: SharedPreferences.Editor

    private lateinit var userId : String

    private lateinit var token: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharePreference = getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)

        editor  = sharePreference.edit()

        userId = sharePreference.getString("USER_ID","").toString()
        token = sharePreference.getString("TOKEN","").toString()


         val myDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)
         binding.dateEditText.setText(myDate.toString())

        binding.createButton.setOnClickListener {
            if( binding.nameEditText.text.isNotEmpty() &&
                binding.descriptionEditText.text.isNotEmpty() &&
                binding.dateEditText.text.isNotEmpty()){

                val requestName = binding.nameEditText.text.toString()
                val requestDescription = binding.descriptionEditText.text.toString()
                val requestDate = binding.dateEditText.text.toString()

                createNewRequest(userId,token,requestName,requestDescription,requestDate)

            }
        }

        binding.backButton.setOnClickListener {
            val intent = Intent(this@CreateRequestActivity,MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createNewRequest(
        userId: String,
        token: String,
        requestName: String,
        requestDescription: String,
        requestDate: String
    ) {
        val data = JsonObject()
        data.addProperty("userId", userId)
        data.addProperty("token",token)
        data.addProperty("name",requestName)
        data.addProperty("description",requestDescription)
        data.addProperty("date",requestDate)
        val api = ServiceBuilder.buildService(Api::class.java)
        api.createNewRequest(data).enqueue(object:Callback<Request>{
            override fun onResponse(call: Call<Request>, response: Response<Request>) {
                    if(response.isSuccessful){
                        val intent = Intent(this@CreateRequestActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
            }

            override fun onFailure(call: Call<Request>, t: Throwable) {
                Log.e("CreateRequestActivity",t.message.toString())
            }

        })
    }
}