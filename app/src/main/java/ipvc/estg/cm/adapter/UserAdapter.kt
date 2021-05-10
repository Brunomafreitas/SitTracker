package ipvc.estg.cm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.cm.R
import ipvc.estg.cm.api.User
import kotlinx.android.synthetic.main.activity_ocorrencias.*

class UserAdapter(private val users: List<User>, context: Context,
                  private val listener : OnItemClickListener): RecyclerView.Adapter<UserAdapter.UsersViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerline, parent,false)
        return UsersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size;
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        return holder.bind(users[position])
    }


    interface OnItemClickListener {
        fun onItemClick(position: Int);
    }

    inner class UsersViewHolder(itemView : View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        private val name: TextView = itemView.findViewById(R.id.nameUser)
        private val tipoId: TextView = itemView.findViewById(R.id.tipoId)
        private val corpo : TextView = itemView.findViewById(R.id.corpoOcorr)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position : Int = adapterPosition

            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position);
            }
        }
        fun bind(user: User){
            name.text ="Utilizador :  " + user.nome;
            if( user.tipo_id == "1"){
                tipoId.text = "acidente";
            }else{
                tipoId.text = "obras"
            }
            //tipoId.text = user.tipo_id
            corpo.text = user.corpo
        }
    }

    fun getIndiceOcorrencia(position: Int) : User{
        return users[position]
    }
}