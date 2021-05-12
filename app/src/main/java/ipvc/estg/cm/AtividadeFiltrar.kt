package ipvc.estg.cm

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import ipvc.estg.cm.adapter.UserAdapter
import ipvc.estg.cm.api.ServiceBuilder
import ipvc.estg.cm.api.User
import ipvc.estg.cm.api.endPoints
import kotlinx.android.synthetic.main.activity_ocorrencias.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AtividadeFiltrar : AppCompatActivity(), UserAdapter.OnItemClickListener {
    private lateinit var users: List<User>
    lateinit var adapter : UserAdapter;
var idUserAtual : String? = " ";
    lateinit var buttonVoltarMapa: Button;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocorrencias)

    }
    public override fun onResume() {
        super.onResume()

        val extras = intent.extras
        val id = extras?.getString("utilizadorId")
        idUserAtual = id;

        buttonVoltarMapa = findViewById<Button>(R.id.VoltarMapa);
        buttonVoltarMapa.setOnClickListener {
            val intent = Intent(this@AtividadeFiltrar, mapaOcorrencias::class.java)
            intent.putExtra("id", idUserAtual);
            startActivity(intent)
        }
        val request = ServiceBuilder.buildService(endPoints::class.java)
        val call = request.getOcorrencias()


        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {

                    adapter = UserAdapter(response.body()!!,this@AtividadeFiltrar, this@AtividadeFiltrar);
                    recyclerview.adapter = adapter;
                    recyclerview.layoutManager = LinearLayoutManager(this@AtividadeFiltrar);

                }
            }
            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(this@AtividadeFiltrar, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    override fun onItemClick(position: Int) {

        if(idUserAtual == adapter.getIndiceOcorrencia(position).users_id) {
            Toast.makeText(this, "Item $position clicado", Toast.LENGTH_SHORT).show();

            val intent = Intent(this, ocorrenciaclicada::class.java);
            intent.putExtra("tituloNotaOcorr", adapter.getIndiceOcorrencia(position).titulo);
            intent.putExtra("corpoNotaOcorr", adapter.getIndiceOcorrencia(position).corpo);
            intent.putExtra("idOcorr", adapter.getIndiceOcorrencia(position).id);
            intent.putExtra("idUtilizador", adapter.getIndiceOcorrencia(position).users_id);
            intent.putExtra("utilizadorId", idUserAtual);
            startActivity(intent);

        } else{
            Toast.makeText(this@AtividadeFiltrar, "Não pode abrir esta ocorrência!", Toast.LENGTH_SHORT).show()
        }
    }


}
