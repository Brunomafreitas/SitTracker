package ipvc.estg.cm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

class ocorrencias : AppCompatActivity(), UserAdapter.OnItemClickListener {
    lateinit var adapter : UserAdapter;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocorrencias)

        val request = ServiceBuilder.buildService(endPoints::class.java)
        val call = request.getUsers()

        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>){
                adapter = UserAdapter( response.body()!!,this@ocorrencias, this@ocorrencias)
                if(response.isSuccessful){


                    recyclerview.apply {
                        recyclerview.adapter = adapter;
                        recyclerview.layoutManager = LinearLayoutManager(this@ocorrencias);

                    }
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(this@ocorrencias, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getSingle(view: View){

    }

    override fun onItemClick(position: Int) {
        Toast.makeText(this, "Item $position clicado" , Toast.LENGTH_SHORT).show();

        val intent = Intent(this, atividadeNotaClicada::class.java);
        intent.putExtra("tituloNota", adapter.getIndiceOcorrencia(position).titulo);
        intent.putExtra("corpoNota", adapter.getIndiceOcorrencia(position).corpo);
        intent.putExtra("id", adapter.getIndiceOcorrencia(position).id);

        startActivity(intent);
    }
}
