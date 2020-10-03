package com.futureWork.Bombay.Send;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SendControl {
	private final SendRepo repo;
	
	public SendControl(SendRepo repo){
		this.repo = repo;
	}
	
	@GetMapping("/send")
	List<Send> all() {
		return repo.findAll();
	}
	
	@PostMapping
	Send newSend(@RequestBody Send msg) {
		/***
		 * here send the data to the receiver 
		 * if receiver not found then save the data until the receiver comes back
		 * and add the exception is the data saved longer then 60 days the delete the data
		 */
		if(true)
			return repo.save(msg);
		/*
		 * else return repo.save(message);
		 */
	}
	
	@GetMapping("/send/{id}")
	Send one(@PathVariable String id) {
		return repo.findById(id).orElseThrow(()->new SendNotFoundException(id));
	}
}
