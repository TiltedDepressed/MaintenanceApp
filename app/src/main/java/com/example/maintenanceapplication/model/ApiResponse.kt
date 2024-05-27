package com.example.maintenanceapplication.model

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("result") var result : List<T>
)
