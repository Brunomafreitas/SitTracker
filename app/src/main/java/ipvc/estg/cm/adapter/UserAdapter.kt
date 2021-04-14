package ipvc.estg.cm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.cm.R
import ipvc.estg.cm.api.User
import kotlinx.android.synthetic.main.activity_ocorrencias.*

class UserAdapter(private val users: List<User>): RecyclerView.Adapter<UserAdapter.UsersViewHolder>() {
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

    class UsersViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        private val name: TextView = itemView.findViewById(R.id.nameUser)
        private val email: TextView = itemView.findViewById(R.id.email)
        private val city: TextView = itemView.findViewById(R.id.city)

        fun bind(user: User){
            name.text = user.name;
            email.text = user.email
        }
    }
}