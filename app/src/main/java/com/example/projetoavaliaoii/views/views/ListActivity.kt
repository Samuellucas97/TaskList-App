package com.example.projetoavaliaoii.views.views

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projetoavaliaoii.R
import com.example.projetoavaliaoii.views.broadcastreceiver.MyBroadcastReceiver
import com.example.projetoavaliaoii.views.notification.NotificationUtils
import com.example.projetoavaliaoii.views.repository.TarefaSQLiteRepository
import com.example.projetoavaliaoii.views.adapter.TarefaAdapter
import com.example.projetoavaliaoii.views.model.Tarefa
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.dialog_my.view.*

class ListActivity : AppCompatActivity() {

    private lateinit var tarefaSQLiteRepository: TarefaSQLiteRepository
    private var tarefas = mutableListOf<Tarefa>()
    private var adapter = TarefaAdapter(tarefas, this::onTarefaItemLongClick, this::onTarefaItemClick)
    private var receiver: MyBroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        receiver = MyBroadcastReceiver()
        var intentFilter = IntentFilter()
        intentFilter.addAction("com.example.projetoavaliaoii.views.broadcastreceiver.MyBroadcastReceiver")
        registerReceiver(receiver,intentFilter)

        tarefaSQLiteRepository = TarefaSQLiteRepository(this)
        initRecyclerView()
    }

    override fun onDestroy() {
        if(receiver != null)
            unregisterReceiver(receiver)
        super.onDestroy()
    }

    private fun initRecyclerView(){
        initTarefas()
        rvTarefas.adapter = adapter
        rvTarefas.layoutManager = LinearLayoutManager(this)
    }

    private fun initTarefas(){
        val tarefasAll = tarefaSQLiteRepository.searchAll()

        for ( i in tarefasAll.indices)
            tarefas.add(tarefasAll[i])
    }

    /// MENU -------------------------------------------------------------------

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actions,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_cadastrar) {
            val intent = Intent(this, CadastraTarefaActivity::class.java)
            startActivityForResult(intent,10)
            return true

        } else if (id == R.id.action_concluida) {

            for ( i in tarefas.indices){
                if (tarefas[i].checked == 0)
                    atualizandoTarefa(tarefas[i])
            }
            return true

        } else if (id == R.id.action_sair) {
            finish()
            return true

        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    /// Métodos de click do recyclerView

    private fun onTarefaItemLongClick(tarefa: Tarefa): Boolean{


        var id = tarefaSQLiteRepository.search(tarefa.title)?.id?.toInt()

        /// DIALOG -------------------------------------------------

        val myDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_my, null)
        val myDialogBuilder = AlertDialog.Builder(this)
            .setView(myDialogView)
            .setTitle("Alerta!")
            .setMessage("Deseja realmente remover a tarefa?")

        val mAlertDialog = myDialogBuilder.show()
        myDialogView.btnOk.setOnClickListener {
            mAlertDialog.dismiss()

            if (tarefa.checked == 0) {

                var intent = Intent("com.example.projetoavaliaoii.views.broadcastreceiver.MyBroadcastReceiver")
                intent.putExtra("id", id)
                intent.putExtra("title", tarefa.title)
                intent.putExtra("description", "")
                sendBroadcast(intent)
            }
            else {

                var positionPost = findPosition(tarefa)
                tarefas.removeAt(positionPost)
                tarefaSQLiteRepository.remove(tarefa)
                adapter.notifyItemRemoved(positionPost)
                rvTarefas.adapter = adapter
            }

        }

        myDialogView.btnCancelar.setOnClickListener {
            mAlertDialog.dismiss()
        }

        return true
    }


    private fun onTarefaItemClick(tarefa: Tarefa) {

        atualizandoTarefa(tarefa)
    }

    fun atualizandoTarefa(tarefa: Tarefa) {
        if (tarefa.checked == 1)
            tarefa.checked = 0
        else
            tarefa.checked = 1

        var positionPost = findPosition(tarefa)

        adapter.notifyItemChanged(positionPost)
        tarefaSQLiteRepository.save(tarefa)
        rvTarefas.adapter = adapter
    }

    /// Demais métodos (inclusive os privados)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 10 && resultCode == Activity.RESULT_OK){
            data?.let {
                val title: String? = data.getStringExtra("Title")
                val description: String? = data.getStringExtra("Description")

                if( description != null && title != null) {
                    addTarefa(title, description)
                }

            }
        }
    }

    private fun addTarefa(titulo: String, description: String){

        var tarefaInBank = tarefaSQLiteRepository.search(titulo)

        if (tarefaInBank != null)
            Toast.makeText(this, "Erro! Essa tarefa já está cadastrada!", Toast.LENGTH_SHORT).show()
        if (tarefaInBank == null) {
            val tarefa = Tarefa(
                title = titulo,
                body = description
            )

            tarefaSQLiteRepository.save(tarefa)
            tarefas.add(tarefa)
            adapter.notifyItemInserted(tarefas.lastIndex)
            rvTarefas.adapter = adapter

            NotificationUtils.notificationSimple(this, tarefaSQLiteRepository.search(titulo)?.id!!.toInt(), titulo, description)
        }
    }

    private fun findPosition(tarefa: Tarefa): Int {

        var positionTarefa = -1
        for ( i in tarefas.indices) {
            if( tarefas[i] === tarefa){
                positionTarefa = i
                break
            }
        }

        return positionTarefa
    }
}
