   package com.futurework.codefriends

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ContextThemeWrapper
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.futurework.codefriends.adapters.Chat2Adapter
import com.futurework.codefriends.Database.DbHelper
import com.futurework.codefriends.Database.MessageDb.MessageDbProvider
import com.futurework.codefriends.Database.UserDb.UserDbProvider
import com.futurework.codefriends.Service.Service
import com.futurework.codefriends.data.Chat2Data
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity2 : AppCompatActivity() {

    private val TAG : String = "ChatActivity2"
    private val dataList  : ArrayList<Chat2Data> = ArrayList()
    private lateinit var adapter : Chat2Adapter
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var recycleView : RecyclerView
    private lateinit var sendButton : FloatingActionButton
    private lateinit var msgBox : AppCompatEditText
    private lateinit var name : AppCompatTextView
    private lateinit var userId : String
    private lateinit var sendId : String
    private lateinit var userImage : AppCompatImageView
    private val database = Firebase.database
    private val mRef = database.getReference("Chatting")
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat2)

        name = findViewById(R.id.name_chat2)
        name.text = UserDbProvider(this).user.name
        sendId = UserDbProvider(this).user.id

        userImage = findViewById(R.id.image_chat2)
        Log.d(TAG,"UserImages/${Service().removeExtraFromString(UserDbProvider(this).user.email)}.jpeg")
        FirebaseStorage.getInstance().reference.child("UserImages/${Service().removeExtraFromString(UserDbProvider(this).user.email)}")
                .downloadUrl.addOnSuccessListener {
                    Glide.with(this)
                            .load(it)
                            .into(userImage)
                }.addOnFailureListener {
                    Glide.with(this)
                            .load(R.drawable.ic_baseline_person_24)
                            .into(userImage)
                }


        val imageSet = findViewById<AppCompatImageView>(R.id.image_set_chat2)
        userId  = this@ChatActivity2.intent.getStringExtra("id").toString()

        val menu : ImageView = findViewById(R.id.menu_chat2)
        menu.setOnClickListener {

            val wrapper: Context = ContextThemeWrapper(this, R.style.ProgressDialog)

            val popupMenu = PopupMenu(wrapper, menu)
            popupMenu.menuInflater.inflate(R.menu.info_holder_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
                when (it.itemId) {
                    R.id.add_image ->
                        Toast.makeText(this, "Add Image", Toast.LENGTH_LONG).show()
                    R.id.add_code ->
                        Toast.makeText(this, "Add Image", Toast.LENGTH_LONG).show()
                    R.id.add_contact ->
                        Toast.makeText(this, "Add Image", Toast.LENGTH_LONG).show()
                    R.id.add_doc ->
                        Toast.makeText(this, "Add Image", Toast.LENGTH_LONG).show()
                    R.id.add_file ->
                        Toast.makeText(this, "Add Image", Toast.LENGTH_LONG).show()
                }
                true
            })

            popupMenu.show()

        }

        //Send button to send msg on firebase add it on database
        sendButton = findViewById(R.id.send_chat2)
        sendButton.setOnClickListener(){
            if(msgBox.text.toString().trim().isNotEmpty()){
                val date = SimpleDateFormat("HH.mm.ss.SS").format(System.currentTimeMillis())
                mRef.child("$userId/").push().setValue(Chat2Data(sendId,msgBox.text.toString().trim(),null,null, date.trim(),Chat2Data.RECEIVER_TEXT))
                MessageDbProvider(DbHelper(this).writableDatabase).setMessage(Chat2Data.SENDER_TEXT,msgBox.text.toString().trim(),date.trim(),userId)
                dataList.add(Chat2Data(sendId, msgBox.text.toString().trim() ,null, null, SimpleDateFormat("HH.mm.ss.SS").format(System.currentTimeMillis()).toString(), Chat2Data.SENDER_TEXT))
                this.msgBox.text?.clear()
                adapter.notifyDataSetChanged()
            }
        }

        /**
         * TODO Button toggle for sending
         */
        msgBox = findViewById(R.id.send_input_text_chat2)
        msgBox.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().trim().isNotEmpty()){
                    sendButton.setImageResource(R.drawable.ic_baseline_send_24)
                    imageSet.setImageResource(R.drawable.ic_baseline_attachment_24)
                }else{
                    imageSet.setImageResource(R.drawable.blank)
                    sendButton.setImageResource(R.drawable.ic_baseline_attachment_24)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        /** All Recycle view work */
        manager = LinearLayoutManager(this)
        this.adapter = Chat2Adapter(dataList)

        this.recycleView = findViewById<RecyclerView>(R.id.recyclerView_chat2).apply{
            layoutManager = manager
            adapter = this@ChatActivity2.adapter
        }
        /**RecycleView Work Done**/
    }

    override fun onStart() {
        super.onStart()
        val s = dataList.size
        //this.dataList.add(Chat2Data("AbcEdible","hi it's working","12/1/2021",Chat2Data.SENDER_TEXT))
        //Check for Database to get all the message
        Log.d(TAG,"Id of user $userId")
        val read = MessageDbProvider(DbHelper(this).readableDatabase).getMessage(userId,2000)
        if(s < read.size){
            for (d in read) {
                Log.d(TAG,"data is read from database type : ${d.type}")
                dataList.add(Chat2Data(d.senderId, d.textMsg.toString().trim(), d.date, d.type))
            }
            adapter.notifyDataSetChanged()
        }
    }

}