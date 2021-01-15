package com.futurework.codefriends.data

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
data class Chat2Data (var senderId : String
                      , var textMsg : String
                      , var ImageMsg : String?
                      , var audioMsg : String?
                      , var date : String?
                      , var type : Int )  {

    companion object{
        const val SENDER_TEXT : Int = 7;
        const val RECEIVER_TEXT : Int  = 6;
        const val RECEIVER_IMAGE : Int = 19;
        const val SENDER_IMAGE : Int = 22;
        const val RECEIVER_AUDIO : Int = 27;
        const val SENDER_AUDIO : Int = 28;
    }


    constructor(senderId: String, textMsg: String, date : String? , type: Int) : this(
            senderId,
            textMsg,
            null,
            null,
            date,
            type
    )

    init{
        if(this.date == null){
            this.date =  SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis())
        }
    }

}