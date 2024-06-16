package com.example.maintenanceapplication.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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
     private  var checkAutoLogIn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        sharePreference = getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)
        checkAutoLogIn = sharePreference.getBoolean("REMEMBER_ME", false)
        binding = ActivityRegBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.continueButton.setOnClickListener{

            login =  binding.loginEditText.text.toString()
            password = binding.passwordEditText.text.toString()
            email = binding.emailEditText.text.toString()

            if(correctPassword(password,applicationContext)){

                if(login.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()){
                    createAccount(login,password,email)
                }

            }
        }

        autoLogIn()


        binding.alreadyHaveAccountTextView.setOnClickListener {
            val intent = Intent(this@RegActivity,LoginActivity::class.java)
            startActivity(intent)
        }


    }


    private fun autoLogIn() {
        if(checkAutoLogIn){
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

//Пароль должен быть не менее 8 символов.
//В нем должна быть хотя бы 1 строчная и хотя бы 1 заглавная буква.
//Он должен иметь один специальный символ, например ! или + или - или подобное
//Он должен содержать хотя бы 1 цифру
fun correctPassword(password: String, applicationContext: Context): Boolean {
    if (password.isEmpty()){
        Toast.makeText(applicationContext, "Пароль должен быть не менее 8 символов", Toast.LENGTH_SHORT).show()
        return false
    }
    if (password.length < 8) {
        Toast.makeText(applicationContext, "Пароль должен быть не менее 8 символов", Toast.LENGTH_SHORT).show()
        return false
    }
    if (password.filter { it.isDigit() }.firstOrNull() == null){
        Toast.makeText(applicationContext, "Пароль должен содержать хотя бы 1 цифру", Toast.LENGTH_SHORT).show()
        return false
    }
    if (password.filter { it.isLetter() }.filter { it.isUpperCase() }.firstOrNull() == null){
        Toast.makeText(applicationContext, "В пароле должна быть хотябы одна заглавная буква", Toast.LENGTH_SHORT).show()
        return false
    }
    if (password.filter { it.isLetter() }.filter { it.isLowerCase() }.firstOrNull() == null){
        Toast.makeText(applicationContext, "В пароле должна быть хотябы одна строчная буква", Toast.LENGTH_SHORT).show()
        return false
    }
    if (password.filter { !it.isLetterOrDigit() }.firstOrNull() == null){
        Toast.makeText(applicationContext, "Пароль должен иметь один специальный символ", Toast.LENGTH_SHORT).show()
        return false
    }
    return true
}


