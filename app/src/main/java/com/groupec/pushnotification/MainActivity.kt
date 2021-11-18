package com.groupec.pushnotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId


class MainActivity : AppCompatActivity() {

    private lateinit var progressbar : ProgressBar
    private lateinit var textViewToken : TextView
    private lateinit var textViewMessage : TextView
    private lateinit var buttonOpenDashboard : Button
    private lateinit var buttonCopyToken : Button

    //Defined the required values
    companion object {
        const val CHANNEL_ID = "groupec"
        private const val CHANNEL_NAME= "GROUPE C TECHNOLOGIES "
        private const val CHANNEL_DESC = "Android Push Notification Tutorial"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressbar = findViewById(R.id.progressbar) as ProgressBar
        textViewToken =  findViewById(R.id.textViewToken) as TextView
        textViewMessage =  findViewById(R.id.textViewMessage) as TextView
        buttonOpenDashboard = findViewById(R.id.buttonOpenDashboard) as Button
        buttonCopyToken = findViewById(R.id.buttonCopyToken) as Button

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                //hiding the progressbar
                progressbar.visibility = View.INVISIBLE

                if (!task.isSuccessful) {
                    //displaying the error if the task is unsuccessful
                    textViewToken.text = task.exception?.message

                    //stopping the further execution
                    return@OnCompleteListener
                }

                //Getting the token if everything is fine
                val token = task.result?.token

                textViewMessage.text = "Your FCM Token is:"
                textViewToken.text = token
                Log.i("token", ""+token)

            })

        //creating notification channel if android version is greater than or equals to oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = CHANNEL_DESC
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        //opening dashboard
        buttonOpenDashboard.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://pushnotification-46dd3.web.app/")
            startActivity(i)
        }

        //copying the token
        buttonCopyToken.setOnClickListener {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("token", textViewToken.text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this@MainActivity, "Copied", Toast.LENGTH_LONG).show()
        }

    }
}