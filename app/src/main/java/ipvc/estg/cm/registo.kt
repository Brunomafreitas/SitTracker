package ipvc.estg.cm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import ipvc.estg.cm.api.OutputPost
import ipvc.estg.cm.api.ServiceBuilder
import ipvc.estg.cm.api.endPoints
import kotlinx.android.synthetic.main.activity_add_ocorrencia.*
import kotlinx.android.synthetic.main.activity_registo.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class registo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registo)


        val button = findViewById<Button>(R.id.buttonCriarUser)
        button.setOnClickListener {
            val nome = insereUsername.text.toString()
            val pass = inserePass.text.toString()

            val request = ServiceBuilder.buildService(endPoints::class.java)


            if (nome.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this@registo, "Insira um nome", Toast.LENGTH_SHORT).show()
            } else {
                val call = request.registo(nomeUser = nome, passUser = pass)
                call.enqueue(object : Callback<OutputPost> {
                    override fun onResponse(
                        call: Call<OutputPost>,
                        response: Response<OutputPost>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body()?.error == false) {
                                Toast.makeText(
                                    this@registo,
                                    "Conta criada!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                //val intent = Intent(this@AddAcidente, MapsActivity::class.java)
                                finish()
                            }
                        }
                    }

                    override fun onFailure(call: Call<OutputPost>, t: Throwable) {
                        Toast.makeText(this@registo, "${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

}
