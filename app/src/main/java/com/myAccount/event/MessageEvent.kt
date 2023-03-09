package com.myAccount.event

import android.service.notification.StatusBarNotification

class MessageEvent(statusBarNotification: StatusBarNotification) {
  val sbn = statusBarNotification
}