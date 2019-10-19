package com.example.projetoavaliaoii.views.repository

import com.example.projetoavaliaoii.views.model.Tarefa

interface TarefaRepository {
    fun save(tarefa: Tarefa)
    fun remove( vararg posts: Tarefa)
    fun searchAll(): List<Tarefa>
    fun search(title: String): Tarefa?
}