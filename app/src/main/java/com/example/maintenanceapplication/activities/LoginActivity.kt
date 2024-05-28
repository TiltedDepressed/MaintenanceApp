package com.example.maintenanceapplication.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.maintenanceapplication.R
import com.example.maintenanceapplication.databinding.ActivityLoginBinding
import com.example.maintenanceapplication.datasource.ServiceBuilder
import com.example.maintenanceapplication.interfaces.Api
import com.example.maintenanceapplication.model.User
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var login: String

    private lateinit var password: String

    private lateinit var sharePreference: SharedPreferences

    private var rememberMe: Boolean? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            val intent = Intent(this@LoginActivity,RegActivity::class.java)
            startActivity(intent)
        }

      binding.continueButton.setOnClickListener {
          login = binding.loginEditText.text.toString()
          password = binding.passwordEditText.text.toString()
          if(login.isNotEmpty() && password.isNotEmpty()){
              signIn(login,password)
          }
      }


    }

    private fun signIn(login: String, password: String) {

        sharePreference = getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharePreference.edit()


        val data = JsonObject()
        data.addProperty("login",login)
        data.addProperty("password",password)
        val api = ServiceBuilder.buildService(Api::class.java)
        api.signIn(data).enqueue(object: Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                 if(response.isSuccessful){
                     rememberMe = true
                     response.body()?.let { user->
                         editor.putString("USER_ID",user.userId)
                         editor.putString("TOKEN", user.token)
                         editor.putString("ROLE",user.role)
                         editor.putBoolean("REMEMBER_ME",rememberMe!!)
                     }
                     editor.apply()

                     val intent = Intent(this@LoginActivity,PinActivity::class.java)
                     startActivity(intent)
                 }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("LoginActivity",t.message.toString())
            }

        })
    }
}