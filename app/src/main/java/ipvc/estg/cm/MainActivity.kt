package ipvc.estg.cm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.buttonNotas)
        button.setOnClickListener{
            val intent = Intent(this, NotasActiviy::class.java)
            startActivity(intent)
        }

        val buttonLogin1 = findViewById<Button>(R.id.logintButton)
        buttonLogin1.setOnClickListener{
            val intent = Intent(this, ocorrencias::class.java)
            startActivity(intent)
        }
    }
}
