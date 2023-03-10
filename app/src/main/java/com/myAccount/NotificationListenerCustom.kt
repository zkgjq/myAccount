package com.myAccount

import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.myAccount.callback.NotifyHelper
import com.myAccount.event.OnListenerStateEvent
import com.myAccount.utils.Utils
import org.greenrobot.eventbus.EventBus

/**
 * Subclass of [NotificationListenerService] used to catch post/remove notification events.
 */
class NotificationListenerCustom: NotificationListenerService() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("NOTIFICATION", "onStartCommand")
        requestRebind(ComponentName(applicationContext, this.javaClass))
        return START_STICKY
    }


    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        Log.d("NOTIFICATION_SERVICE", "NOTIFICATION POSTED")
        sbn?.let {
            clearUnnecessaryMessage(it)

            displayOnLogNotificationInfo(it)
            NotifyHelper.onReceive(it)
        }
    }

    private fun clearUnnecessaryMessage(sbn: StatusBarNotification?){
        val appName = sbn?.let { Utils.getAppName(it.packageName) }
        if(!appName.equals("微信") && !appName.equals("QQ")&&!appName.equals("Kim")){
            if (sbn != null) {
                cancelNotification(sbn.key)
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        Log.d("NOTIFICATION_SERVICE", "NOTIFICATION REMOVED")
        sbn?.let {
            NotifyHelper.onRemoved(it)
        }
    }

    /**
     * Test function to display on log notification information.
     */
    private fun displayOnLogNotificationInfo(sbn: StatusBarNotification) {
        val appName = Utils.getAppName(sbn.packageName)
//        if(appName == "QQ" || appName == "微信" || appName == "com.myAccount"){
//            EventBus.getDefault().post(MessageEvent(sbn))
//        }
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
        Log.d("NOTIFICATION", "onDestroy: ")

    }

    override fun onCreate() {
        super.onCreate()
        Log.d("NOTIFICATION", "onCreate: ")
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d("NOTIFICATION", "onListenerConnected: ")
        Toast.makeText(applicationContext, "监听服务已打开", Toast.LENGTH_SHORT).show()
        EventBus.getDefault().post(OnListenerStateEvent(1))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        Log.d("NOTIFICATION", "onListenerDisconnected: ")
        EventBus.getDefault().post(OnListenerStateEvent(0))
    }

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }

}