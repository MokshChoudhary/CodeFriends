package com.futurework.codefriends.adapters

import android.content.Context
import android.util.Log
import androidx.appcompat.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.futurework.codefriends.R
import com.futurework.codefriends.data.Chat2Data

class Chat2Adapter(private val dataList : ArrayList<Chat2Data>) : RecyclerView.Adapter<Chat2Adapter.ViewHolder>() {

    private val TAG : String = "Chat2Adapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View
        Log.d(TAG,"View find $viewType")
        when (viewType) {
            Chat2Data.SENDER_TEXT -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.chat_massage_send_layout,parent,false)
                return ViewHolder(view)
            }
            Chat2Data.RECEIVER_TEXT -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.chat_massage_recive_layout,parent,false)
                return ViewHolder(view)
            }
            Chat2Data.RECEIVER_IMAGE -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.chat_image_receiver_layout,parent,false)
                return ViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.chat_image_send_layout,parent,false)
                return ViewHolder(view)
            }
        }
    }

   override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindItem(dataList[position])


    override fun getItemViewType(position: Int): Int =
            when (dataList[position].type) {
            Chat2Data.SENDER_TEXT -> {
                Chat2Data.SENDER_TEXT
            }
            Chat2Data.RECEIVER_TEXT -> {
                Chat2Data.RECEIVER_TEXT
            }
            Chat2Data.RECEIVER_IMAGE -> {
                Chat2Data.RECEIVER_IMAGE
            }
            else -> {
                Chat2Data.SENDER_IMAGE
            }
        }


    override fun getItemCount(): Int = dataList.size


    class ViewHolder(view : View) : RecyclerView.ViewHolder(view),View.OnLongClickListener{
        private lateinit var textMsg : TextView
        private lateinit var date : TextView
        private lateinit var imageMsg : ImageView
        private lateinit var audioMsg : Any
        private var v :View = view
        fun bindItem(data : Chat2Data){
            when(data.type){
                Chat2Data.SENDER_TEXT -> {
                    this.textMsg = v.findViewById(R.id.chat_massage_send_text)
                    date = v.findViewById(R.id.chat_massage_send_date)
                    this.textMsg.text = data.textMsg
                    date.text = data.date
                }
                Chat2Data.RECEIVER_TEXT -> {
                    textMsg = v.findViewById(R.id.chat_massage_receive_text)
                    date = v.findViewById(R.id.chat_massage_receive_date)
                    textMsg.text = data.textMsg
                    date.text = data.date
                }
                Chat2Data.SENDER_IMAGE -> {
                    imageMsg = v.findViewById(R.id.send_image_layout)
                    date = v.findViewById(R.id.send_data_layout)
                    Glide.with(v)
                            .load(data.ImageMsg)
                            .into(imageMsg)
                    date.text = data.date
                }
                Chat2Data.RECEIVER_IMAGE -> {
                    imageMsg = v.findViewById(R.id.receive_image_layout)
                    date = v.findViewById(R.id.receive_data_layout)
                    Glide.with(v)
                            .load(data.ImageMsg)
                            .into(imageMsg)
                    date.text = data.date
                }
            }
        }

        override fun onLongClick(p0: View?): Boolean {
            val wrapper : Context = ContextThemeWrapper(this.v.context,R.style.ProgressDialog)
            val popupMenu = PopupMenu(wrapper,p0)
            popupMenu.menuInflater.inflate(R.menu.info_holder_menu,popupMenu.menu)
            popupMenu.show()

            return true
        }
    }

}

