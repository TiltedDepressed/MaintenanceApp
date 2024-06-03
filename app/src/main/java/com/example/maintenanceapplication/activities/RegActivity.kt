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
import com.example.maintenanceapplication.databinding.ActivityRegBinding
import com.example.maintenanceapplication.datasource.ServiceBuilder
import com.example.maintenanceapplication.interfaces.Api
import com.example.maintenanceapplication.model.User
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegActivity : AppCompatActivity() {
     private lateinit var binding: ActivityRegBinding
     private lateinit var  login: String
     private lateinit var password: String
     private lateinit var email: String
     private lateinit var sharePreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.continueButton.setOnClickListener{

            login =  binding.loginEditText.text.toString()
            password = binding.passwordEditText.text.toString()
            email = binding.emailEditText.text.toString()

            if(login.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()){
                createAccount(login,password,email)
            }
        }

        autoLogIn()


        binding.alreadyHaveAccountTextView.setOnClickListener {
            val intent = Intent(this@RegActivity,LoginActivity::class.java)
            startActivity(intent)
        }


    }

    private fun autoLogIn() {
        sharePreference = getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)
        if(sharePreference.getBoolean("REMEMBER_ME", false)){
            val intent = Intent(this@RegActivity, PinActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createAccount(login: String, password: String, email: String) {

        sharePreference = getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharePreference.edit()

        val data = JsonObject()
        data.addProperty("login",login)
        data.addProperty("password",password)
        data.addProperty("email",email)
        val api = ServiceBuilder.buildService(Api::class.java)
        api.signUp(data).enqueue(object: Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                        response.body().let { user ->
                            editor.putString("login",login)
                            editor.putString("password",password)
                            editor.putString("email",email)

                      editor.apply()
                    }

                    val intent = Intent(this@RegActivity,LoginActivity::class.java)
                    startActivity(intent)
                }
                if(response.code() == 409){
                    Toast.makeText(applicationContext, "User already exist", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("RegActivity",t.message.toString())
            }

        })
    }
}