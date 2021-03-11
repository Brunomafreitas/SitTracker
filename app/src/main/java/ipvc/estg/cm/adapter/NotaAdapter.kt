package ipvc.estg.cm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.cm.ENTIDADES.notasPessoais
import ipvc.estg.cm.R
import kotlinx.android.synthetic.main.recyclerviewitem.view.*

class NotaAdapter internal constructor(
    context: Context
): RecyclerView.Adapter<NotaAdapter.NotasViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notas = emptyList<notasPessoais>()


    class NotasViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val notaItemView: TextView = itemView.findViewById(R.id.tituloNota)
        val descNotaItemView: TextView = itemView.findViewById(R.id.corpoNota)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotasViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerviewitem, parent, false)
        return NotasViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotasViewHolder, position: Int) {
        val current = notas[position];
        holder.notaItemView.text = current.tituloNota;
        holder.descNotaItemView.text = current.corpoNota;
    }

    internal fun setNota(notas: List<notasPessoais>){
        this.notas = notas;
        notifyDataSetChanged();
    }

    override fun getItemCount(): Int {
        return notas.size;
    }
}