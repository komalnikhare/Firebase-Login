package com.example.firebaselogin.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaselogin.R


class ListAdapter(
    final val context: Context,
    private var messageList: List<String>
) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    public fun setList(list: ArrayList<String>) {
        messageList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val country = messageList[position]
        holder.tvMessage.setText(country)

    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class ViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        var tvMessage: TextView
        init {
            tvMessage = itemView.findViewById(R.id.tvMessage)

        }
    }


}
