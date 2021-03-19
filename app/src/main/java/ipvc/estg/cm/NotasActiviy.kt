package ipvc.estg.cm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ipvc.estg.cm.ENTIDADES.notasPessoais
import ipvc.estg.cm.ViewModel.NotasViewModel
import ipvc.estg.cm.adapter.NotaAdapter
import kotlinx.android.synthetic.main.activity_notas_activiy.*
import kotlinx.android.synthetic.main.recyclerviewitem.*

class NotasActiviy : AppCompatActivity(), NotaAdapter.OnItemClickListener {
private lateinit var notasViewModel: NotasViewModel;
    private val newWordActivityRequestCode = 1;
     lateinit var recyclerView : RecyclerView;
     lateinit var adapter : NotaAdapter;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas_activiy)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        adapter = NotaAdapter(this,this);
        recyclerView.adapter = adapter;
        recyclerView.layoutManager = LinearLayoutManager(this);


        notasViewModel = ViewModelProvider(this).get(NotasViewModel::class.java)
        notasViewModel.allNotes.observe(this, Observer{ notas ->
        notas?.let {adapter.setNota(it)}
        })




        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent (this, AddNota::class.java)
            startActivityForResult(intent, newWordActivityRequestCode);

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK){

            val ptitulo  =data?.getStringExtra(AddNota.EXTRA_REPLY)
            val pCorpo = data?.getStringExtra(AddNota.EXTRA_REPLY1)


                if(ptitulo != null && pCorpo != null) {
                         val nota1 = notasPessoais(tituloNota = ptitulo.toString(), corpoNota = pCorpo.toString())

                        notasViewModel.insert(nota1)
        }

        }else{
            Toast.makeText(applicationContext, "Nota não inserida", Toast.LENGTH_SHORT).show()
        }


    }

    override fun onItemClick(position: Int) {
        Toast.makeText(this, "Item $position clicado" , Toast.LENGTH_SHORT).show();

        val intent = Intent(this, atividadeNotaClicada::class.java);
        intent.putExtra("tituloNota", adapter.getIndiceNota(position).tituloNota);
        intent.putExtra("corpoNota", adapter.getIndiceNota(position).corpoNota);
        intent.putExtra("id", adapter.getIndiceNota(position).id);

        startActivity(intent);
    }


}
