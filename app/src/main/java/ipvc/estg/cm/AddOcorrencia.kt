package ipvc.estg.cm

import android.content.Intent
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
            val replyIntent = Intent()
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
                //val lat = 0.0
                //val call = request.add_ocorrencias(titulo = "a", corpo = "a", user_id = 1, lat = lat.toFloat() , lng = lat.toFloat(), tipo_id = 1)
                call.enqueue(object : Callback<OutputPost> {
                    override fun onResponse(call: Call<OutputPost>, response: Response<OutputPost>) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == true) {
                                replyIntent.putExtra(EXTRA_REPLYID, id)
                                replyIntent.putExtra(EXTRA_REPLY,titulo)
                                replyIntent.putExtra(EXTRA_REPLY1,corpo)
                                replyIntent.putExtra(EXTRA_REPLY2,lat)
                                replyIntent.putExtra(EXTRA_REPLY3,lng)
                                replyIntent.putExtra(EXTRA_REPLYTIPOID,recebeTipoAcidente)
                                finish()
                                Toast.makeText(
                                    this@AddOcorrencia,
                                    "Ocorrencia introduzida com exito",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                    override fun onFailure(call: Call<OutputPost>, t: Throwable) {
                        Toast.makeText(this@AddOcorrencia, "${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
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
        const val EXTRA_REPLYID = "id"
        const val EXTRA_REPLY = "textoOcorr"
        const val EXTRA_REPLY1 = "corpoOcorr"
        const val EXTRA_REPLY2 = "latOcorr"
        const val EXTRA_REPLY3 = "lngOcorr"
        const val EXTRA_REPLYTIPOID = "tipo_id"
    }
}
