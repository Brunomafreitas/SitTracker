package ipvc.estg.cm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ipvc.estg.cm.api.OutputPost
import ipvc.estg.cm.api.ServiceBuilder
import ipvc.estg.cm.api.endPoints
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Login : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val irparaRegisto = findViewById<Button>(R.id.irRegisto)
        irparaRegisto.setOnClickListener {
            val intent = Intent(this@Login, registo::class.java)       // Abrir as notas
            startActivity(intent)
        }
        val notas = findViewById<Button>(R.id.notas_button)
        notas.setOnClickListener {
            val intent = Intent(this@Login, NotasActiviy::class.java)       // Abrir as notas
            startActivity(intent)
        }


        val button = findViewById<Button>(R.id.activity_main_loginButton)
        button.setOnClickListener {
            val nome_user = nome_user.text.toString().trim()
            val pass = pass.text.toString().trim()

            val request = ServiceBuilder.buildService(endPoints::class.java)
            val call = request.login(nome_user, pass)

            if(nome_user.isEmpty() || pass.isEmpty())
            {
                Toast.makeText(this@Login, "Introduza o username e a password", Toast.LENGTH_SHORT).show()

            }
            else {

                call.enqueue(object : Callback<OutputPost> {
                    override fun onResponse(call: Call<OutputPost>, response: Response<OutputPost>) {

                        if (response.isSuccessful) {   //Se a resposta for positiva na camada de serviços significa que o utilizador existe
                            if (response.body()?.error == false) {
                                Toast.makeText(
                                    this@Login,
                                    "Username ou palavra passe incorreta",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {          // Se der tudo certo

                                val check1: CheckBox = findViewById(R.id.checkbox)
                                val id = response.body()?.title.toString()
                                if (check1.isChecked){

                                    //Toast.makeText(this@Login, "Toogle On", Toast.LENGTH_SHORT).show()

                                    var token = getSharedPreferences("loginutilizador", Context.MODE_PRIVATE)
                                    var editor = token.edit()
                                    intent.putExtra("utilizador", nome_user)
                                    intent.putExtra("utilizador", id)

                                    editor.putString("loginutilizador", nome_user)
                                    editor.putString("loginid", id)
                                    editor.commit()
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                                }
                                Toast.makeText(this@Login, "Bem vindo " + nome_user, Toast.LENGTH_LONG).show()

                                val intent = Intent(this@Login, mapaOcorrencias::class.java)       // Abrir a main do maps
                                intent.putExtra("id", id)
                                if (check1.isChecked) {
                                    startActivity(intent)
                                    finish()

                                }
                                startActivity(intent)


                            }
                        }
                    }
                    override fun onFailure(call: Call<OutputPost>, t: Throwable) {
                        //Toast.makeText(this@login, "${t.message}", Toast.LENGTH_SHORT).show()
                        Toast.makeText(
                            this@Login,
                            "Username ou palavra passe incorreta",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }
    }

    override fun onStart() {
        super.onStart()
        var token = getSharedPreferences("utilizador", Context.MODE_PRIVATE)
        if (token.getString("loginutilizador", " ") != " ") {
            val intent = Intent(applicationContext, NotasActiviy::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("id", token.getString("loginid", " "))
            finish()
            startActivity(intent)
        }


    }
}
