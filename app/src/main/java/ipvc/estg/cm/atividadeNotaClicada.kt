package ipvc.estg.cm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import ipvc.estg.cm.ENTIDADES.notasPessoais
import ipvc.estg.cm.ViewModel.NotasViewModel
import kotlinx.android.synthetic.main.activity_atividade_nota_clicada.*
import kotlinx.android.synthetic.main.recyclerviewitem.*

class atividadeNotaClicada : AppCompatActivity() {
    lateinit var buttonApagar: Button;
    private lateinit var notasViewModel: NotasViewModel;
    var id : Int = 0;
    lateinit var buttonEditar : Button;
    lateinit var tituloNotaClicados : EditText;
    lateinit var corpoNotaClicados : EditText;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atividade_nota_clicada)
        buttonApagar = findViewById(R.id.buttonDelete);
        buttonEditar = findViewById(R.id.buttonEdit);
        tituloNotaClicados  = findViewById(R.id.tituloNotaClicado)
        corpoNotaClicados  = findViewById(R.id.corpoNotaClicado)

        var buttonEliminar = findViewById<Button>(R.id.buttonDelete)
        val extras : Bundle? = intent.extras;
        var notaTitulo : String = " ";
        var corpoNota : String = " ";
        notasViewModel = ViewModelProvider(this).get(NotasViewModel::class.java)

        if(extras != null){
            notaTitulo = extras.getString("tituloNota").toString()
            corpoNota = extras.getString("corpoNota").toString()
            id = extras.getInt("id")
        }

        tituloNotaClicados.setText(notaTitulo)
        corpoNotaClicados.setText(corpoNota);

        buttonApagar.setOnClickListener {
            onClick();
        }

    buttonEditar.setOnClickListener{
        onClickEditar();
    }

    }

     fun onClick(){
        notasViewModel.deleteNotaById(id)
        val intent = Intent(this, NotasActiviy::class.java)
        startActivity(intent)
    }

    fun onClickEditar(){

        if(TextUtils.isEmpty(tituloNotaClicados.text) || TextUtils.isEmpty(corpoNotaClicados.text)){
            Toast.makeText(this, "O titulo e corpo da nota devem estar preenchidos!", Toast.LENGTH_SHORT).show();
        }else{
            notasViewModel.updateNotaById(
                tituloNota = tituloNotaClicado.text.toString(),
                corpoNota = corpoNotaClicado.text.toString(),
                id = id
            )
            Toast.makeText(this, "Nota editada!", Toast.LENGTH_SHORT).show();
            finish()
        }

    }

    companion object {
        const val EXTRA_REPLY = "ipvc.estg.cm.REPLY"

    }
}
