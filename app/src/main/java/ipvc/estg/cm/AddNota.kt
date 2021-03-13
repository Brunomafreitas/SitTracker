package ipvc.estg.cm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText

class AddNota : AppCompatActivity() {

    private lateinit var editWordView: EditText
    private lateinit var editWordView1: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_nota)

        editWordView = findViewById(R.id.edit_word)
        editWordView1 = findViewById(R.id.edit_word2)
        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if(TextUtils.isEmpty(editWordView.text)){
                setResult(Activity.RESULT_CANCELED, replyIntent)
            }else{
                val note = editWordView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, note)
                setResult(Activity.RESULT_OK, replyIntent)

                val noteCorpo:String = editWordView1.text.toString()
                        replyIntent.putExtra(EXTRA_REPLY1, noteCorpo)

            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "ipvc.estg.cm.REPLY"
        const val EXTRA_REPLY1 = "ipvc.estg.cm.REPLY1"
    }
}
