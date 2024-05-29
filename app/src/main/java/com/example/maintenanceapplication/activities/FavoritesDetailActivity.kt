package com.example.maintenanceapplication.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.maintenanceapplication.R
import com.example.maintenanceapplication.databinding.ActivityFavoriteDetailBinding
import com.example.maintenanceapplication.databinding.ActivityRequestDetailBinding
import com.example.maintenanceapplication.datasource.ServiceBuilder
import com.example.maintenanceapplication.fragments.HistoryFragment
import com.example.maintenanceapplication.fragments.HomeFragment
import com.example.maintenanceapplication.interfaces.Api
import com.example.maintenanceapplication.model.Favorites
import com.example.maintenanceapplication.model.Request
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoritesDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteDetailBinding

    private lateinit var requestID: String

    private lateinit var favoritesId:String

    private lateinit var sharePreference: SharedPreferences

    private lateinit var editor: SharedPreferences.Editor

    private lateinit var token: String

    private lateinit var status: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFavoriteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharePreference = this.getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)
        editor = sharePreference.edit()
        requestID = intent.getStringExtra("REQUEST_ID").toString()
        token = sharePreference.getString("TOKEN","").toString()
        favoritesId = intent.getStringExtra("FAVORITES_ID").toString()

        getRequestData(requestID,token)

        binding.backButton.setOnClickListener {
            val intent = Intent(this@FavoritesDetailActivity,MainActivity::class.java)
            startActivity(intent)
        }

        binding.favoriteImageView.setOnClickListener {
                 deleteFromFavorites(favoritesId,token)
        }

    }

    private fun deleteFromFavorites(favoritesId: String, token: String) {
        val data = JsonObject()
        data.addProperty("token",token)
        val api = ServiceBuilder.buildService(Api::class.java)
        api.deleteFavoritesByFavoritesId(favoritesId,data).enqueue(object:Callback<Favorites>{
            override fun onResponse(call: Call<Favorites>, response: Response<Favorites>) {
                if(response.isSuccessful){
                    val intent = Intent(this@FavoritesDetailActivity,MainActivity::class.java)
                    startActivity(intent)
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