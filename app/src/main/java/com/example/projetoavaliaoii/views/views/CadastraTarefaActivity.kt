package com.example.projetoavaliaoii.views.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.projetoavaliaoii.R
import com.example.projetoavaliaoii.views.notification.NotificationUtils
import kotlinx.android.synthetic.main.activity_cadastra_tarefa.*

class CadastraTarefaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastra_tarefa)

        btnCadastrarTarefa.setOnClickListener {
            val titulo = edtTituloTarefa.text.toString()
            val description =  edtDescricaoTarefa.text.toString()


            if ( titulo != "" && description != "") {
                val intent: Intent = Intent()
                intent.putExtra("Title", titulo)
                intent.putExtra("Description",description)

                setResult(RESULT_OK,intent)
                finish()
            }

        }
    }
}
