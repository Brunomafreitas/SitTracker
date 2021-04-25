package ipvc.estg.cm.api



data class User (
    val id: Int,
    val name: String,
    val lat: String,
    val lng: String,
    val tipo_id: String,
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



