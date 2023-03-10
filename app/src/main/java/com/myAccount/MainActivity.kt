package com.myAccount

import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.service.notification.StatusBarNotification
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.myAccount.callback.NotifyHelper
import com.myAccount.callback.NotifyListener
import com.myAccount.databinding.ActivityMainBinding
import com.myAccount.event.EventUtils
import com.myAccount.event.OnListenerStateEvent
import com.myAccount.utils.Utils
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivity : AppCompatActivity() {

  companion object {
    private const val NOTIFICATION_PERMISSION =
      "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
    private const val TABLE_NAME = "enabled_notification_listeners"
  }

  private lateinit var binding: ActivityMainBinding
  private lateinit var notificationCreator: NotificationCreator
  private lateinit var manager: NotificationManager

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
    manager = getSystemService(Context.NOTIFICATION_SERVICE)
        as NotificationManager
    notificationCreator = NotificationCreator(this)
    init()

    handleButtons()
    handleNotify()

  }

  // 用来解决杀掉进程之后再次启动不触发监听的问题
  private fun init() {
    val pm = packageManager
    pm.setComponentEnabledSetting(
      ComponentName(this, NotificationListenerCustom::class.java),
      PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP
    )
    pm.setComponentEnabledSetting(
      ComponentName(this, NotificationListenerCustom::class.java),
      PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
    )


  }

  private fun handleNotify() {
    NotifyHelper.setNotifyListener(object : NotifyListener {
      @RequiresApi(Build.VERSION_CODES.O)
      override fun onReceiveMessage(sbn: StatusBarNotification) {
        binding.appName.text = Utils.getAppName(sbn.packageName)
//                binding.appName.text = sbn.key
        binding.postTime.text = Utils.convertUnixTimestampToTime(sbn.postTime)
        binding.title.text = sbn.notification.extras.getCharSequence("android.title")
        binding.text.text = sbn.notification.extras.getCharSequence("android.text")

      }

      override fun onRemoveMessage(sbn: StatusBarNotification) {

      }

    })
  }



  override fun onResume() {
    super.onResume()
    updateTextPermissionColor(checkNotificationListenerServiceAccess())
  }


  /**
   * Handles click events on buttons.
   */
  private fun handleButtons() {
    binding.buttonRequestAccess.setOnClickListener {
      val intentAccess = Intent(NOTIFICATION_PERMISSION)
      startActivity(intentAccess)
    }
    binding.buttonNotification.setOnClickListener { notificationCreator.createNotification() }

  }

  /**
   * Checks if application has access to the notification settings.
   * @return : access status
   */
  private fun checkNotificationListenerServiceAccess(): Boolean {
    val componentService = ComponentName(this, NotificationListenerCustom::class.java)
    val apps = Settings.Secure.getString(contentResolver, TABLE_NAME)

    return apps.contains(componentService.flattenToString())
  }

  /**
   * Updates color text.
   */
  @Suppress("Deprecation")
  private fun updateTextPermissionColor(status: Boolean) {
    fun updateWithResources(@ColorRes color: Int, @StringRes text: Int) {
      binding.textPermission.apply {
        setText(text)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
          setTextColor(resources.getColor(color, null))
        else setTextColor(resources.getColor(color))
      }
    }
    if (status) updateWithResources(R.color.green, R.string.notification_access_granted)
    else updateWithResources(R.color.red, R.string.notification_access_denied)
  }

  override fun onStart() {
    super.onStart()
    EventUtils.registerSafely(this)
  }

  override fun onDestroy() {
    super.onDestroy()
    EventUtils.unRegisterSafely(this)
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  fun onServiceCreateEvent(serviceCreateEvent: OnListenerStateEvent) {
    binding.serviceState.apply {
      if (serviceCreateEvent.state == 1) {
        setText(R.string.service_open)
        setTextColor(resources.getColor(R.color.green))
      } else {
        setText(R.string.service_close)
        setTextColor(resources.getColor(R.color.red))
      }

    }
  }
}