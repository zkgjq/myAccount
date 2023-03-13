package com.myAccount.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myAccount.R
import com.myAccount.utils.Utils

class ExpenseAdapter(private val dataset:List<String>):RecyclerView.Adapter<ExpenseAdapter.ViewHolder>
  () {

  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val detail: TextView = view.findViewById(R.id.detail)
  }
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.expense_item, parent,false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
   holder.detail.text = dataset[position]
  }

  override fun getItemCount(): Int {

    return dataset.size
  }

}