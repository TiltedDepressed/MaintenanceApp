package com.example.maintenanceapplication.interfaces
import com.example.maintenanceapplication.model.ApiResponse
import com.example.maintenanceapplication.model.Favorites
import com.example.maintenanceapplication.model.User
import com.example.maintenanceapplication.model.Request
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface Api {

    //User
    @Headers("Content-Type:application/json")
    @POST("user/login")
    fun signIn(
        @Body body: JsonObject
    ): Call<User>

    @Headers("Content-Type:application/json")
    @POST("user/signup")
    fun signUp(
        @Body body: JsonObject
    ): Call<User>

    @Headers("Content-Type:application/json")
    @POST("user/{userId}")
     fun getUserById(
        @Path("userId") id: String,
        @Body body: JsonObject
    ): Call<User>

    @Headers("Content-Type:application/json")
    @POST("user/delete/{userId}")
    fun deleteUserById(
        @Path("userId") id: String,
        @Body body: JsonObject
    ): Call<User>

    @Headers("Content-Type:application/json")
    @POST("user/update/{userId}")
    fun updateUserById(
        @Path("userId") id : String,
        @Body body: JsonObject
    ): Call<User>

    @Headers("Content-Type:application/json")
    @POST("user/role/{role}")
    fun getAllUsersByRole(
        @Path("role") id : String,
        @Body body: JsonObject
    ): Call<ApiResponse<User>>

    //Request
    @Headers("Content-Type:application/json")
    @POST("request")
    fun getAllRequests(): Call<ApiResponse<Request>>

    @Headers("Content-Type:application/json")
    @POST("request/create")
    fun createNewRequest(
        @Body body: JsonObject
    ): Call<Request>

    @Headers("Content-Type:application/json")
    @POST("request/find/{userId}")
    fun findRequestByUserId(
        @Path("userId") id : String,
        @Body body: JsonObject
    ): Call<ApiResponse<Request>>

    @Headers("Content-Type:application/json")
    @POST("request/find/id/{requestId}")
    fun findRequestByRequestId(
        @Path("requestId") id : String,
        @Body body:JsonObject
    ): Call<Request>

    @Headers("Content-Type:application/json")
    @POST("request/delete/{requestId}")
    fun deleteRequestByRequestId(
        @Path("requestId") id : String,
        @Body body : JsonObject
    ): Call<Request>

    @Headers("Content-Type:application/json")
    @POST("request/update/{requestId}")
    fun updateRequestByRequestId(
        @Path("requestId") id : String,
        @Body body: JsonObject
    ): Call<Request>


    //Favorites
    @Headers("Content-Type:application/json")
    @POST("favorites")
    fun getAllFavorites(): Call<ApiResponse<Request>>

    @Headers("Content-Type:application/json")
    @POST("favorites/create")
    fun createNewFavorites(
        @Body body: JsonObject
    ): Call<Favorites>

    @Headers("Content-Type:application/json")
    @POST("favorites/find/fav/{userId}")
    fun findFavoritesByUserId(
        @Path("userId") id : String,
        @Body body: JsonObject
    ): Call<ApiResponse<Favorites>>


    @Headers("Content-Type:application/json")
    @POST("favorites/delete/{favoritesId}")
    fun deleteFavoritesByFavoritesId(
        @Path("favoritesId") id : String,
        @Body body : JsonObject
    ): Call<Favorites>

    @Headers("Content-Type:application/json")
    @POST("favorites/update/{favoritesId}")
    fun updateFavoritesByRequestId(
        @Path("favoritesId") id : String,
        @Body body: JsonObject
    ): Call<Favorites>

}