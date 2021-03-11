package ipvc.estg.cm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ipvc.estg.cm.ENTIDADES.notasPessoais
import ipvc.estg.cm.ViewModel.NotasViewModel
import ipvc.estg.cm.adapter.NotaAdapter

class NotasActiviy : AppCompatActivity() {
private lateinit var notasViewModel: NotasViewModel;
    private val newWordActivityRequestCode = 1;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas_activiy)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NotaAdapter(this);
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
            data?.getStringExtra(AddNota.EXTRA_REPLY)?.let {
                val nota = notasPessoais(tituloNota = it, corpoNota = "Algo para fazer mais tarde")
                notasViewModel.insert(nota)
            }
        }else{
            Toast.makeText(applicationContext, "Nota não inserida", Toast.LENGTH_SHORT).show()
        }
    }
}
