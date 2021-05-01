package ipvc.estg.cm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

class AtividadeFiltrar : AppCompatActivity() {
    private lateinit var users: List<User>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocorrencias)


        val request = ServiceBuilder.buildService(endPoints::class.java)
        val call = request.ordenaPortipo()


        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    recyclerview.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(this@AtividadeFiltrar)
                        adapter = UserAdapter(response.body()!!)
                    }
                }
            }
            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(this@AtividadeFiltrar, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }



}
