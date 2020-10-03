package com.futureWork.Bombay.Send;

public class SendNotFoundException extends RuntimeException{
	SendNotFoundException(String id){
		super("Could not find any message " + id);
	}
}
