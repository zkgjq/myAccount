package com.myAccount.callback

import android.service.notification.StatusBarNotification

object NotifyHelper {
  private var notifyListener: NotifyListener? = null
  fun onReceive(sbn: StatusBarNotification){
    notifyListener?.onReceiveMessage(sbn)

  }

  fun onRemoved(sbn: StatusBarNotification){
    notifyListener?.onReceiveMessage(sbn)
  }

  fun setNotifyListener(listener: NotifyListener){
    notifyListener = listener
  }
}