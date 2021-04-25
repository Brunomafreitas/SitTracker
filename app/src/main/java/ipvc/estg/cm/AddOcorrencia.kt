package ipvc.estg.cm

import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ipvc.estg.cm.api.OutputPost
import ipvc.estg.cm.api.ServiceBuilder
import ipvc.estg.cm.api.endPoints
import kotlinx.android.synthetic.main.activity_add_ocorrencia.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddOcorrencia : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ocorrencia)


        val extras = intent.extras

        val lat = extras?.getString("lat")
        val lng = extras?.getString("lng")
        val id = extras?.getString("id")

        Toast.makeText(this@AddOcorrencia, lat +" | "+ lng+" | "+ id, Toast.LENGTH_SHORT).show()










        val button = findViewById<Button>(R.id.butaoSalvar)
        button.setOnClickListener {
            val titulo = descricao.text.toString()
            val corpo = corpo.text.toString()
            val tipoAcidente = tipoAcidente.text.toString();
            var recebeTipoAcidente : Int = 0;
            val request = ServiceBuilder.buildService(endPoints::class.java)

if(tipoAcidente != "1" || tipoAcidente != "0"){
    Toast.makeText(this@AddOcorrencia, "Esse tipo de acidente não existe", Toast.LENGTH_SHORT).show()
        }

            if(tipoAcidente == "1"){
                recebeTipoAcidente = 1;
            }else if(tipoAcidente == "0"){
                recebeTipoAcidente = 0;
            }
            if (titulo.isEmpty()) {
                Toast.makeText(this@AddOcorrencia, "Preencher descrição", Toast.LENGTH_SHORT).show()
            }
            else {
                val call = request.add_ocorrencias(titulo = titulo, corpo = corpo, user_id = id?.toInt(), lat = lat?.toFloat(), lng = lng?.toFloat(), tipo_id = recebeTipoAcidente)
                call.enqueue(object : Callback<OutputPost> {
                    override fun onResponse(call: Call<OutputPost>, response: Response<OutputPost>) {
                        if (response.isSuccessful) {
                            if (response.body()?.error == false) {
                                Toast.makeText(
                                    this@AddOcorrencia,
                                    "Ocorreu um erro. Não foi possivel introduzir a ocorrencia",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            else {
                                Toast.makeText(
                                    this@AddOcorrencia,
                                    "Ocorrencia introduzido com exito.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                //val intent = Intent(this@AddAcidente, MapsActivity::class.java)
                                finish()
                                //startActivity(intent)

                            }
                        }
                    }
                    override fun onFailure(call: Call<OutputPost>, t: Throwable) {
                        Toast.makeText(this@AddOcorrencia, "${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }

            //Toast.makeText(this@AddAcidente, descricao+ " | "+lng, Toast.LENGTH_SHORT).show()

        }

    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        parent.getItemAtPosition(pos)
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }





    companion object {
        const val EXTRA_REPLY_ID = "com.example.android.id"

    }
}
