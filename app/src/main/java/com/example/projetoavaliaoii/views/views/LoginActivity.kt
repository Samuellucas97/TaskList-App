package com.example.projetoavaliaoii.views.views

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.projetoavaliaoii.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        /// SHARED PREFERENCES

        var pref = getSharedPreferences("configuracoes",0)
        var login = pref.getString("Email", "")
        var senha = pref.getString("Senha", "")

        edtEmailLogin.setText(login)
        edtSenhaLogin.setText(senha)

        /// ------------------------

        btnCadastrarLogin.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivityForResult(intent, 10)
        }

        btnEntrarLogin.setOnClickListener {

            login = pref.getString("Email", "")
            senha = pref.getString("Senha", "")

            if ( login !== "" && senha !== "") {
                val intent = Intent(this, ListActivity::class.java)
                startActivity(intent)
            }
            else  Toast.makeText(this, "Usuário não cadastrado!", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 10 && resultCode == Activity.RESULT_OK){
            data?.let {
                val email: String? = data.getStringExtra("email")
                val senha: String? = data.getStringExtra("senha")

                edtEmailLogin.setText(email)
                edtSenhaLogin.setText(senha)

            }
        }
    }




}
