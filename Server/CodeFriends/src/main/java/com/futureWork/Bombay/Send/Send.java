package com.futureWork.Bombay.Send;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Objects;
@Entity
public class Send {
	private @Id String id;
	private long sender;
	private long reciver;
	private int message_type;
	private String message;
	private ArrayList<String> messages;
	private Timestamp timestamp;
	
	Send(){}
	
	public Send(String id, long sender, long reciver, int message_type, String message, Timestamp timestamp){
		this.id = id;
		this.sender = sender;
		this.reciver = reciver;
		this.message = message;
		this.message_type = message_type;
		this.timestamp = timestamp;
	}
	
	public Send(String id, long sender, long reciver, int message_type, ArrayList<String> messages, Timestamp timestamp){
		this.id = id;
		this.sender = sender;
		this.reciver = reciver;
		this.messages = messages;
		this.message_type = message_type;
		this.timestamp = timestamp;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getSender() {
		return sender;
	}
	public void setSender(long sender) {
		this.sender = sender;
	}
	public long getReciver() {
		return reciver;
	}
	public void setReciver(long reciver) {
		this.reciver = reciver;
	}
	public int getMessage_type() {
		return message_type;
	}
	public void setMessage_type(int message_type) {
		this.message_type = message_type;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	@Override
	public boolean equals(Object o) {
		if(this == o)
			return true;
		
		if(!(o instanceof Send)) 
			return false;
			
		Send sender = (Send)o;
		return Objects.equals(this.id, sender.id) && Objects.equals(this.sender, sender.sender)
				&& Objects.equals(this.reciver, sender.reciver) && Objects.equals(this.message_type, sender.message_type)
				&& Objects.equals(this.message, sender.message) && Objects.equals(this.timestamp, sender.timestamp);
	}
	
	@Override 
	public int hashCode() {
		return Objects.hash(this.id,this.sender,this.reciver,this.message_type,this.message,this.timestamp);
	}
	
	@Override
	public String toString() {
		return "Message id: "+this.id+" Message sender: "+this.sender+" Message reciver: "+this.reciver+" Message type: "+this.message_type+" Message content: "+this.message+" Message timeStamp"+this.timestamp;
	}
}
