package ipvc.estg.cm.api

import retrofit2.Call
import retrofit2.http.*

interface endPoints {


    @GET("/api/mapas")
    fun getUsers(): Call<List<User>>

    @GET("/users/{id}")
    fun getUserById(@Path("id") id:Int): Call<User>

    @FormUrlEncoded
    @POST("/posts")
    fun postTest (@Field("title") first: String?): Call<OutputPost>

    @GET("/meuslim/api/maps2/{nome_user}")
    fun getIdByUser(@Path("nome_user") nome_user: String?): Call<List<iduser>>

    @FormUrlEncoded
    @POST("/api/userLog/login")
    fun login(  @Field("nome") first: String?,
                @Field("PASSWORD") pass: String?): Call<OutputPost>

    @FormUrlEncoded
    @POST("/api/Registo")
    fun registo(  @Field("nome") nomeUser: String?,
                  @Field("PASSWORD") passUser: String?): Call<OutputPost>



    @FormUrlEncoded
    @POST("/api/add_ocorrencia")
    fun add_ocorrencias(   @Field("titulo") titulo: String,
                        @Field("corpo") corpo: String,
                        @Field("user_id") user_id: Int?,
                        @Field("lat") lat: Float?,
                        @Field("lng") lng: Float? ,
                        @Field("tipo_id") tipo_id: Int?) : Call<OutputPost>
}