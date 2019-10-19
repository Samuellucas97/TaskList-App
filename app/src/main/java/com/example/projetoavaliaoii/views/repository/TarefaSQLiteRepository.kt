package com.example.projetoavaliaoii.views.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.projetoavaliaoii.views.model.Tarefa


class TarefaSQLiteRepository(ctx: Context): TarefaRepository {

    private val helper: TarefaSqlHelper = TarefaSqlHelper(ctx)

    /// MÉTODOS PÚBLICOS  ------------------------------------------------------------------------------------

    override fun save(tarefa: Tarefa) {
        if (tarefa.id == 0L) {
            insert(tarefa)
        } else {
            update(tarefa)
        }
    }

    override fun remove(vararg tarefas: Tarefa) {
        val db = helper.writableDatabase

        for (tarefa in tarefas){

            db.delete(
                TABLE_NAME,
                "$COLUMN_TITLE = ? ",
                arrayOf(tarefa.title.toString())
            )
        }
        db.close()
    }

    override fun searchAll(): MutableList<Tarefa> {
        val tarefas = ArrayList<Tarefa>()
        val db = helper.writableDatabase
        var cursor: Cursor? = null

        cursor = db.rawQuery("select * from $TABLE_NAME", null)

        var id: Long
        var title: String
        var description: String
        var completed: Int

        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
                title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))
                description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION))
                completed = cursor.getInt(cursor.getColumnIndex(COLUMN_COMPLETED))

                tarefas.add(Tarefa(id, title, description, completed))
                cursor.moveToNext()
            }
        }
        return tarefas
    }


    //:: No banco está configurado para que o título seja único
    override fun search(title: String): Tarefa? {
        var especificTarefa = searchAll().filter { it.title == title}

        if ( especificTarefa.size > 0)
            return especificTarefa[0]

        return null


    }

    /// MÉTODOS PRIVADOS ------------------------------------------------------------------------------------

    private fun insert(tarefa: Tarefa){
        val db = helper.writableDatabase

        val cv = ContentValues().apply {
            put(COLUMN_TITLE, tarefa.title)
            put(COLUMN_DESCRIPTION, tarefa.body)
            put(COLUMN_COMPLETED, tarefa.checked)
        }

        val id = db.insert(TABLE_NAME, null, cv)

        if (id != -1L)
            tarefa.id = id

        db.close()
    }

    private fun update(tarefa: Tarefa) {
        val db = helper.writableDatabase

        val cv = ContentValues().apply {
            put(COLUMN_TITLE, tarefa.title)
            put(COLUMN_DESCRIPTION, tarefa.body)
            put(COLUMN_COMPLETED, tarefa.checked)
        }

        db.update(
            TABLE_NAME,
            cv,
            "$COLUMN_TITLE = ? ",
            arrayOf(tarefa.title.toString())
        )
        db.close()
    }


}