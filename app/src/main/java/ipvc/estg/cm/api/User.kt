package ipvc.estg.cm.api

import android.graphics.Bitmap


data class User (
    val id: Int,
    val titulo: String,
    val corpo : String,
    val users_id : String,
    var lat: String,
    var lng: String,
    val nome: String,
    val tipo_id: String,
    val imagem: Bitmap,




    val email: String
   // val adress: Address
)

data class Address(
        val city: String,
        val zipcode: String
)


data class iduser(
    val id: String?
)



