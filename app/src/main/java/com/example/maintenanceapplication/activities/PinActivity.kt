package com.example.maintenanceapplication.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.maintenanceapplication.R
import com.example.maintenanceapplication.databinding.ActivityPinBinding
import com.example.maintenanceapplication.datasource.ServiceBuilder
import com.example.maintenanceapplication.interfaces.Api
import com.example.maintenanceapplication.model.User
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPinBinding

    private lateinit var sharePreference: SharedPreferences

    private var pinLengthCounter: Int = 0

    private var pinMaxLength: Int = 4

    private var pinMinLength: Int = 0

    private var pin: String = ""

    private var buffer: String = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPinBinding.inflate(layoutInflater)
        setContentView(binding.root)


        sharePreference = getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharePreference.edit()

        binding.logOutImageView.setOnClickListener {
            editor.clear()
            editor.apply()
            val intent = Intent(this@PinActivity,LoginActivity::class.java)
            startActivity(intent)
        }

        val userID : String = sharePreference.getString("USER_ID", "").toString()
        val token: String = sharePreference.getString("TOKEN","").toString()

        getUserData(userID,token)

        binding.clearImageView.setOnClickListener {
            if(pinLengthCounter > pinMinLength){
                pinLengthCounter--
                pinCounter(pinLengthCounter)
                minusPin()
            }
        }

        binding.pinButtonOne.setOnClickListener {
            if(pinLengthCounter < pinMaxLength){
                pinLengthCounter++
                pinCounter(pinLengthCounter)
                currentPIN(buffer+"1")


            }
        }
        binding.pinButtonTwo.setOnClickListener {
            if(pinLengthCounter < pinMaxLength){
                pinLengthCounter++
                pinCounter(pinLengthCounter)
                currentPIN(buffer+"2")
            }
        }
        binding.pinButtonThree.setOnClickListener {
            if(pinLengthCounter < pinMaxLength){
                pinLengthCounter++
                pinCounter(pinLengthCounter)
                currentPIN(buffer+"3")
            }
        }
        binding.pinButtonFour.setOnClickListener {
            if(pinLengthCounter < pinMaxLength){
                pinLengthCounter++
                pinCounter(pinLengthCounter)
                currentPIN(buffer+"4")
            }
        }
        binding.pinButtonFive.setOnClickListener {
            if(pinLengthCounter < pinMaxLength){
                pinLengthCounter++
                pinCounter(pinLengthCounter)
                currentPIN(buffer+"5")
            }
        }
        binding.pinButtonSix.setOnClickListener {
            if(pinLengthCounter < pinMaxLength){
                pinLengthCounter++
                pinCounter(pinLengthCounter)
                currentPIN(buffer+"6")
            }
        }
        binding.pinButtonSeven.setOnClickListener {
            if(pinLengthCounter < pinMaxLength){
                pinLengthCounter++
                pinCounter(pinLengthCounter)
                currentPIN(buffer+"7")
            }
        }
        binding.pinButtonEight.setOnClickListener {
            if(pinLengthCounter < pinMaxLength){
                pinLengthCounter++
                pinCounter(pinLengthCounter)
                currentPIN(buffer+"8")
            }
        }
        binding.pinButtonNine.setOnClickListener {
            if(pinLengthCounter < pinMaxLength){
                pinLengthCounter++
                pinCounter(pinLengthCounter)
                currentPIN(buffer+"9")
            }
        }
        binding.pinButtonZero.setOnClickListener {
            if(pinLengthCounter < pinMaxLength){
                pinLengthCounter++
                pinCounter(pinLengthCounter)
                currentPIN(buffer+"0")
            }
        }



        binding.continueButton.setOnClickListener {
            getUserData(userID, token)
        }

    }

    private fun minusPin(): String {
         pin = pin.substring(0,pin.length - 1 )
         return pin
    }

    private fun currentPIN(newNumber: String) {
        pin += newNumber
    }


    private fun pinCounter(pinLengthCounter: Int) {
        if(pinLengthCounter == 0){
            binding.pinCounterFirstCircle.background = ContextCompat.getDrawable(this@PinActivity,R.drawable.circle_pin_counter)
            binding.pinCounterSecondCircle.background = ContextCompat.getDrawable(this@PinActivity,R.drawable.circle_pin_counter)
            binding.pinCounterThirdCircle.background = ContextCompat.getDrawable(this@PinActivity,R.drawable.circle_pin_counter)
            binding.pinCounterFourthCircle.background = ContextCompat.getDrawable(this@PinActivity,R.drawable.circle_pin_counter)
        }

        if(pinLengthCounter == 1){
            binding.pinCounterFirstCircle.background = ContextCompat.getDrawable(this@PinActivity,R.drawable.circle)
            binding.pinCounterSecondCircle.background = ContextCompat.getDrawable(this@PinActivity,R.drawable.circle_pin_counter)
            binding.pinCounterThirdCircle.background = ContextCompat.getDrawable(this@PinActivity,R.drawable.circle_pin_counter)
            binding.pinCounterFourthCircle.background = ContextCompat.getDrawable(this@PinActivity,R.drawable.circle_pin_counter)
        }
        if(pinLengthCounter == 2){
            binding.pinCounterSecondCircle.background = ContextCompat.getDrawable(this@PinActivity,R.drawable.circle)
            binding.pinCounterFirstCircle.background = ContextCompat.getDrawable(this@PinActivity,R.drawable.circle)
            binding.pinCounterThirdCircle.background = ContextCompat.getDrawable(this@PinActivity,R.drawable.circle_pin_counter)
            binding.pinCounterFourthCircle.background = ContextCompat.getDrawable(this@PinActivity,R.drawable.circle_pin_counter)
        }
        if(pinLengthCounter == 3){
            binding.pinCounterThirdCircle.background = ContextCompat.getDrawable(this@PinActivity,R.drawable.circle)
            binding.pinCounterSecondCircle.background = ContextCompat.getDrawable(this@PinActivity,R.drawable.circle)
            binding.pinCounterFirstCircle.background = ContextCompat.getDrawable(this@PinActivity,R.drawable.circle)
            binding.pinCounterFourthCircle.background = ContextCompat.getDrawable(this@PinActivity,R.drawable.circle_pin_counter)
        }
        if(pinLengthCounter == 4){
            binding.pinCounterFourthCircle.background = ContextCompat.getDrawable(this@PinActivity,R.drawable.circle)
            binding.pinCounterSecondCircle.background = ContextCompat.getDrawable(this@PinActivity,R.drawable.circle)
            binding.pinCounterThirdCircle.background = ContextCompat.getDrawable(this@PinActivity,R.drawable.circle)
            binding.pinCounterFirstCircle.background = ContextCompat.getDrawable(this@PinActivity,R.drawable.circle)
        }
    }

    private fun getUserData(userID: String, token: String) {
        val data = JsonObject()
        data.addProperty("token",token)
        val api = ServiceBuilder.buildService(Api::class.java)
        api.getUserById(userID,data).enqueue(object:Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                    val responseBody = response.body()!!
                    binding.usernameTextView.text = responseBody.login
                    if(responseBody.pin == ""){

                        binding.pinText.setText(R.string.create_your_pin)
                        if(pinLengthCounter == 4){
                            updateUserPIN(userID,pin,token)
                        }
                    } else {
                        binding.pinText.setText(R.string.enter_your_pin)
                        if(pin == responseBody.pin){
                            val intent = Intent(this@PinActivity, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("PinActivity",t.message.toString())
            }

        })
    }

    private fun updateUserPIN(userID: String, pin: String, token: String) {
        val data = JsonObject()
        data.addProperty("token",token)
        data.addProperty("pin",pin)
        val api = ServiceBuilder.buildService(Api::class.java)
        api.updateUserById(userID,data).enqueue(object:Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                 if(response.isSuccessful){
                     getUserData(userID, token)
                 }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("PinActivity",t.message.toString())
            }

        })
    }
}