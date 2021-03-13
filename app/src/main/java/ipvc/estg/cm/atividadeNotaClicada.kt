package ipvc.estg.cm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.recyclerviewitem.*

class atividadeNotaClicada : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atividade_nota_clicada)

            var tituloNotaClicados  = findViewById<TextView>(R.id.tituloNotaClicado)
        var corpoNotaClicados  = findViewById<TextView>(R.id.corpoNotaClicado)

         var notaTitulo : String = " ";
        var corpoNota : String = " ";
        val extras : Bundle? = intent.extras;
        if(extras != null){
        notaTitulo = extras.getString("tituloNota").toString()
            corpoNota = extras.getString("corpoNota").toString()
        }

        tituloNotaClicados.setText(notaTitulo)
        corpoNotaClicados.setText(corpoNota);
    }
}
