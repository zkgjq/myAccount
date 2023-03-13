package com.myAccount.utils

import android.content.Context
import android.content.SharedPreferences

object SpUtils {
  private var sp:SharedPreferences?=null

  private fun getsp(context: Context):SharedPreferences{
    if(sp==null){
      sp=context.getSharedPreferences("default",Context.MODE_PRIVATE)
    }
    return sp!!
  }

  fun putString(key:String,value:String?,context:Context){
    if(!value.isNullOrBlank()){
      var editor:SharedPreferences.Editor=getsp(context).edit()
      editor.putString(key,value)
      editor.commit()
    }
  }

  fun getString(key:String,context: Context):String?{
    if(!key.isNullOrBlank()){
      var sp: SharedPreferences =getsp(context)
      return sp.getString(key,null)
    }
    return null
  }
}