package com.example.projetoavaliaoii.views.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.projetoavaliaoii.views.notification.NotificationUtils

class MyBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(ctx: Context, intent: Intent) {

        val title: String? = intent.getStringExtra("title")
        val description: String? = intent.getStringExtra("description")
        val id: Int = intent.getIntExtra("id", -1)

        NotificationUtils.notificationSimple(ctx, id, title!!, description!!)

    }
}