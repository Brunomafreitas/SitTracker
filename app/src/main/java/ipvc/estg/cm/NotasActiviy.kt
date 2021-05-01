package ipvc.estg.cm

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ipvc.estg.cm.ENTIDADES.notasPessoais
import ipvc.estg.cm.ViewModel.NotasViewModel
import ipvc.estg.cm.adapter.NotaAdapter
import java.util.*


class NotasActiviy : AppCompatActivity(), NotaAdapter.OnItemClickListener {
private lateinit var notasViewModel: NotasViewModel;
    private val newWordActivityRequestCode = 1;
     lateinit var recyclerView : RecyclerView;
     lateinit var adapter : NotaAdapter;

    private lateinit var prefs: SharedPreferences;
    private var muda : String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas_activiy)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        adapter = NotaAdapter(this, this);
        recyclerView.adapter = adapter;
        recyclerView.layoutManager = LinearLayoutManager(this);



        notasViewModel = ViewModelProvider(this).get(NotasViewModel::class.java)
        notasViewModel.allNotes.observe(this, Observer { notas ->
            notas?.let { adapter.setNota(it) }
        })
       /* val notas = findViewById<Button>(R.id.buttonLogout)
        notas.setOnClickListener {
            var token = getSharedPreferences("utilizador", Context.MODE_PRIVATE)
            intent.putExtra("utilizador", " ")
            var editor = token.edit()
            editor.putString("loginutilizador", " ")
            editor.commit()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            //finish()
            val intent = Intent(this@NotasActiviy, Login::class.java)
            startActivity(intent)
            finish()

        }*/





        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent (this, AddNota::class.java)
            startActivityForResult(intent, newWordActivityRequestCode);

        }



        var mIth = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT
            ) {


                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: ViewHolder,
                    target: ViewHolder
                ): Boolean {
                    var position_dragged:Int = viewHolder.adapterPosition
                    var position_target:Int = target.adapterPosition;

                    Collections.swap(notasViewModel.allNotes.value, position_dragged, position_target);

                    adapter.notifyItemMoved(position_dragged, position_target)

                    return false;
                }

                override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                    // remove from adapter
                }
            })

        mIth.attachToRecyclerView(recyclerView);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK){

            val ptitulo  =data?.getStringExtra(AddNota.EXTRA_REPLY)
            val pCorpo = data?.getStringExtra(AddNota.EXTRA_REPLY1)


                if(ptitulo != null && pCorpo != null) {
                         val nota1 = notasPessoais(tituloNota = ptitulo.toString(), corpoNota = pCorpo.toString())

                        notasViewModel.insert(nota1)
                    Toast.makeText(applicationContext, "Nota inserida", Toast.LENGTH_SHORT).show()
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
