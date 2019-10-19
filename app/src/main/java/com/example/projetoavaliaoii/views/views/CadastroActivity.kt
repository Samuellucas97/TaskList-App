package com.example.projetoavaliaoii.views.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.projetoavaliaoii.R
import kotlinx.android.synthetic.main.activity_cadastra_tarefa.*
import kotlinx.android.synthetic.main.activity_cadastro.*

class CadastroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        /// SHARED PREFERENCES

        val pref = getSharedPreferences("configuracoes",0)
        val edt = pref.edit()

        /// ------------------------

        btnCadastrarCad.setOnClickListener {

            /// Gravando com shared preferences

            val email = edtEmailCad.text.toString()
            val senha = edtSenhaCad.text.toString()

            edt.putString("NomeCompleto", edtNomeCompletoCad.text.toString())
            edt.putString("Email", email)
            edt.putString("Senha", senha)
            edt.commit()

            /// Retonrnando dados para tela de login

            val intent: Intent = Intent()
            intent.putExtra("email", email)
            intent.putExtra("senha", senha)

            setResult(RESULT_OK,intent)
            Toast.makeText(this, "Usuário cadastrado!", Toast.LENGTH_SHORT).show()
            finish()
        }

    }
}
