package com.myAccount.event

import org.greenrobot.eventbus.EventBus

object EventUtils {
  fun registerSafely(target: Any?) {
    if (!EventBus.getDefault().isRegistered(target)) {
      EventBus.getDefault().register(target)
    }
  }

  fun unRegisterSafely(target: Any?) {
    if (EventBus.getDefault().isRegistered(target)) {
      EventBus.getDefault().unregister(target)
    }
  }
}