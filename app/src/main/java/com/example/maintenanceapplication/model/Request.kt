package com.example.maintenanceapplication.model

import com.google.gson.annotations.SerializedName

data class Request(
    @SerializedName("_id")var id: String? = null,
    @SerializedName("name")var name: String? = null,
    @SerializedName("description")var description: String? = null,
    @SerializedName("date")var date: String? = null,
    @SerializedName("status")var status: String? = null,
    @SerializedName("userId")var user: String? = null
)




