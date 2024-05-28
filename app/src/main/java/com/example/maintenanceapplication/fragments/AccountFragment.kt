package com.example.maintenanceapplication.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.maintenanceapplication.activities.LoginActivity
import com.example.maintenanceapplication.activities.RegActivity
import com.example.maintenanceapplication.databinding.FragmentAccountBinding
import com.example.maintenanceapplication.datasource.ServiceBuilder
import com.example.maintenanceapplication.interfaces.Api
import com.example.maintenanceapplication.model.User
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AccountFragment : Fragment() {


    private lateinit var binding: FragmentAccountBinding

    private lateinit var sharePreference: SharedPreferences

    private lateinit var editor: SharedPreferences.Editor

    private lateinit var userID: String

    private lateinit var token : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharePreference = this.requireActivity().getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)
        editor = sharePreference.edit()
         userID = sharePreference.getString("USER_ID","").toString()
         token = sharePreference.getString("TOKEN","").toString()
         getUserData(userID,token)
    }

    private fun getUserData(userID: String, token: String) {
        val data = JsonObject()
        data.addProperty("token",token)
        val api = ServiceBuilder.buildService(Api::class.java)
        api.getUserById(userID,data).enqueue(object: Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
              if(response.isSuccessful){
                  val responseBody = response.body()!!
                  binding.usernameTextView.text =  responseBody.login
                  binding.emailTextView.text = responseBody.email
                  binding.nameEditText.hint = responseBody.login
                  binding.emailEditText.hint = responseBody.email
              }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("AccountFragment",t.message.toString())
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        buttons()
        return binding.root

    }

    private fun buttons() {

        binding.logOutImageView.setOnClickListener {
            editor.clear()
            editor.apply()
            val intent = Intent(this@AccountFragment.requireActivity(), LoginActivity::class.java )
            startActivity(intent)
        }

        binding.deleteButton.setOnClickListener {
          deleteUser(userID,token)
        }

        binding.updateButton.setOnClickListener {
            if(binding.nameEditText.text.isNotEmpty()){
                val data = JsonObject()
                val newName = binding.nameEditText.text.toString()
                data.addProperty("login",newName)
                data.addProperty("token",token)
                updateUserData(userID,data)
                binding.nameEditText.text.clear()
                getUserData(userID, token)
            }
            if(binding.emailEditText.text.isNotEmpty()){
                val data = JsonObject()
                val newEmail = binding.emailEditText.text.toString()
                data.addProperty("email",newEmail)
                data.addProperty("token",token)
                updateUserData(userID,data)
                binding.emailEditText.text.clear()
                getUserData(userID, token)
            }
            if(binding.passwordEditText.text.isNotEmpty()){
                val data = JsonObject()
                val newPassword = binding.passwordEditText.text.toString()
                data.addProperty("login",newPassword)
                data.addProperty("token",token)
                updateUserData(userID,data)
                binding.passwordEditText.text.clear()
                getUserData(userID, token)
            }
        }
    }

    private fun updateUserData(userID: String, data: JsonObject) {
        val api = ServiceBuilder.buildService(Api::class.java)
        api.updateUserById(userID,data).enqueue(object:Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                    Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("AccountFragment",t.message.toString())
            }

        })
    }

    private fun deleteUser(userID: String, token: String) {
        val data = JsonObject()
        data.addProperty("token",token)
        val api = ServiceBuilder.buildService(Api::class.java)
        api.deleteUserById(userID,data).enqueue(object:Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                    editor.clear()
                    editor.apply()
                    val intent = Intent(this@AccountFragment.requireActivity(), RegActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("AccountFragment",t.message.toString())
            }

        })
    }


}