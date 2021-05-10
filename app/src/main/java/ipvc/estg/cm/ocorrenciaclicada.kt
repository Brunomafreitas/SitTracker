package ipvc.estg.cm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import ipvc.estg.cm.api.OutputPost
import ipvc.estg.cm.api.ServiceBuilder
import ipvc.estg.cm.api.endPoints
import kotlinx.android.synthetic.main.activity_atividade_nota_clicada.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ocorrenciaclicada : AppCompatActivity() {
    lateinit var buttonApagar: Button;

    var id : Int = 0;
    lateinit var buttonEditar : Button;
    lateinit var tituloNotaClicados : EditText;
    lateinit var corpoNotaClicados : EditText;
    var notaTitulo : String = " ";
    var corpoNota : String = " ";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocorrenciaclicada)

        buttonApagar = this.findViewById(R.id.buttonDeleteOcurr);
        buttonEditar = findViewById(R.id.buttonEditOcorr);
        tituloNotaClicados = findViewById(R.id.tituloOcorrclicado)
        corpoNotaClicados = findViewById(R.id.corpoOcorrClicado)

        val extras: Bundle? = intent.extras;
        val idUserAtual = extras?.getString("utilizadorId")
        val userIdDanota = extras?.getString("idUtilizador")

        if (extras != null) {
            notaTitulo = extras.getString("tituloNotaOcorr").toString()
            corpoNota = extras.getString("corpoNotaOcorr").toString()
            id = extras.getInt("idOcorr")
        }

        tituloNotaClicados.setText(notaTitulo)
        corpoNotaClicados.setText(corpoNota);

        if (idUserAtual == userIdDanota) {
            buttonEditar.setOnClickListener {
                onClickEditar();
            }

            buttonApagar.setOnClickListener {
                onClickEliminar();
            }
        }
        else{
            Toast.makeText(
                this@ocorrenciaclicada,
                "Não pode editar ou apagar esta nota pois não foi criada por você!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }



    fun onClickEditar(){

        if(TextUtils.isEmpty(tituloNotaClicados.text) || TextUtils.isEmpty(corpoNotaClicados.text)){
            Toast.makeText(this, "O titulo e corpo da nota devem estar preenchidos!", Toast.LENGTH_SHORT).show();
        }else{
            val request = ServiceBuilder.buildService(endPoints::class.java)
            val call = request.getOcorrenciaParaEditar(id,tituloNotaClicados.text.toString(),corpoNotaClicados.text.toString())
            call.enqueue(object : Callback<OutputPost> {
                override fun onResponse(
                    call: Call<OutputPost>,
                    response: Response<OutputPost>
                ) {

                    if (response.isSuccessful) {
                        if (response.body()?.status == false) {
                            Toast.makeText(
                                this@ocorrenciaclicada,
                                "Não foi possivel editar a ocorrência",
                                Toast.LENGTH_SHORT
                            ).show()
                        }else {
                            Toast.makeText(
                                this@ocorrenciaclicada,
                                "Ocorrencia editada com sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()

                            finish()
                        }
                        }
                }

                override fun onFailure(call: Call<OutputPost>, t: Throwable) {
                    Toast.makeText(this@ocorrenciaclicada, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })


        }

    }

    fun onClickEliminar(){

            val request = ServiceBuilder.buildService(endPoints::class.java)
            val call = request.apagaOcorrencia(id, tituloNotaClicados.text.toString())
            call.enqueue(object : Callback<OutputPost> {
                override fun onResponse(
                    call: Call<OutputPost>,
                    response: Response<OutputPost>
                ) {

                    if (response.isSuccessful) {
                        if (!response.body()!!.status) {
                            Toast.makeText(
                                this@ocorrenciaclicada,
                                "Não foi possivel apagar a ocorrencia",
                                Toast.LENGTH_SHORT
                            ).show()
                        }else {
                            Toast.makeText(
                                this@ocorrenciaclicada,
                                "Ocorrencia eliminada com sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()

                            val intent = Intent(this@ocorrenciaclicada, mapaOcorrencias::class.java)

                            startActivity(intent)

                        }
                    }
                }

                override fun onFailure(call: Call<OutputPost>, t: Throwable) {
                    Toast.makeText(this@ocorrenciaclicada, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })



    }
}
