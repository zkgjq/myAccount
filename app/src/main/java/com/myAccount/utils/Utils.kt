package com.myAccount.utils

import java.text.SimpleDateFormat
import java.util.*

object Utils {

  private val packageNameMap = mutableMapOf(
    "com.ss.android.ugc.aweme" to "抖音",
    "com.tencent.mm" to "微信",
    "com.tencent.mobileqq" to "QQ",
    "com.yiqixie.kem" to "Kim",
    "com.notificationservicetest" to "我的测试APP",
    "com.ss.android.auto" to "懂车帝",
    "com.eg.android.AlipayGphone" to "支付宝"
  )

  fun convertUnixTimestampToTime(unixTimestamp: Long): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return dateFormat.format(Date(unixTimestamp))
  }

  fun getAppName(packageName:String): String? {
    if(packageNameMap[packageName] == null){
      return packageName
    }
    return packageNameMap[packageName]
  }
}