package ipvc.estg.cm.api

import retrofit2.Call
import retrofit2.http.*

interface endPoints {


    @GET("/users")
    fun getUsers(): Call<List<User>>

    @GET("/users/{id}")
    fun getUserById(@Path("id") id:Int): Call<User>

    @FormUrlEncoded
    @POST("/posts")
    fun postTest (@Field("title") first: String?): Call<OutputPost>

    @FormUrlEncoded
    @POST("/api/userLog/login")
    fun login(  @Field("nome") first: String?,
                @Field("PASSWORD") pass: String?): Call<OutputPost>

    @FormUrlEncoded
    @POST("/api/registo")
    fun registo(  @Field("nome") nomeUser: String?,
                  @Field("PASSWORD") passUser: String?): Call<OutputPost>


}