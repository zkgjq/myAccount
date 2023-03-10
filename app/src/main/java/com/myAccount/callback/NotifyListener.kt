package com.myAccount.callback

import android.service.notification.StatusBarNotification

interface NotifyListener {
  fun onReceiveMessage(sbn: StatusBarNotification);
  fun onRemoveMessage(sbn: StatusBarNotification)
}