package com.example.projetoavaliaoii.views.adapter

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projetoavaliaoii.R
import com.example.projetoavaliaoii.views.model.Tarefa
import kotlinx.android.synthetic.main.item_tarefa.view.*

class TarefaAdapter(
    private val tarefas: List<Tarefa>,
    private val callback: (Tarefa)-> Boolean,
    private val callbackTwo: (Tarefa) -> Unit
):RecyclerView.Adapter<TarefaAdapter.VH>() {

    class VH(itemView: View): RecyclerView.ViewHolder(itemView){   /// Padrão ViewHolder
        val txtId: TextView = itemView.txtId
        val txtTitle: TextView = itemView.txtTitulo
        val txtBody: TextView = itemView.txtDescricao
        val checked: CheckBox = itemView.checked
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_tarefa,parent,false)

        val vh = VH(v)
        vh.itemView.setOnLongClickListener {
            val tarefa = tarefas[vh.adapterPosition]
            callback(tarefa)
        }

        vh.itemView.setOnClickListener {
            val tarefa = tarefas[vh.adapterPosition]
            callbackTwo(tarefa)
        }

        return vh
    }

    override fun getItemCount(): Int = tarefas.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val (id, title, body, checked) = tarefas[position]

        holder.txtId.text = id.toString()
        holder.txtTitle.text = title
        holder.txtBody.text = body

        if (checked == 1) {
            holder.checked.isChecked = true
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
            holder.txtTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
        if (checked == 0) {
            holder.checked.isChecked = false
        }
    }
}
