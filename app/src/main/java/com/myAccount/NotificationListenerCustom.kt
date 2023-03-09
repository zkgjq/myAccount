package com.myAccount

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.app.NotificationCompat
import com.myAccount.event.MessageEvent
import com.myAccount.utils.Utils
import org.greenrobot.eventbus.EventBus

/**
 * Subclass of [NotificationListenerService] used to catch post/remove notification events.
 */
class NotificationListenerCustom: NotificationListenerService() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("NOTIFICATION", "onStartCommand")
//        init()
        return START_STICKY
    }

    private fun init(){
        val channelId = "ForegroundServiceChannel"
        val channelName = "Foreground Service Channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("My App is running in the foreground")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentText("This is my foreground service notification")
        startForeground(110, notificationBuilder.build())
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        Log.d("NOTIFICATION_SERVICE", "NOTIFICATION POSTED")
        sbn?.let { displayOnLogNotificationInfo(it) }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        Log.d("NOTIFICATION_SERVICE", "NOTIFICATION REMOVED")
    }

    /**
     * Test function to display on log notification information.
     */
    private fun displayOnLogNotificationInfo(sbn: StatusBarNotification) {
        val appName = Utils.getAppName(sbn.packageName)
        if(appName == "QQ" || appName == "微信" || appName == "com.myAccount"){
            EventBus.getDefault().post(MessageEvent(sbn))
        }
        Log.i("NOTIFICATION_SERVICE", "--------------------------------------------------")
        Log.i("NOTIFICATION_SERVICE", "Package name : ${sbn.packageName}")
        Log.i("NOTIFICATION_SERVICE", "Post time : ${sbn.postTime}")
        val notification = sbn.notification
        Log.i("NOTIFICATION_SERVICE", "Title : ${notification.extras
                                                          .getCharSequence("android.title")}")
        Log.i("NOTIFICATION_SERVICE", "Title : ${notification.extras
                                                          .getCharSequence("android.text")}")
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
    }
}