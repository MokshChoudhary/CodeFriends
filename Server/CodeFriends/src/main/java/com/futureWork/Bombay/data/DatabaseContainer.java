package com.futureWork.Bombay.data;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.futureWork.Bombay.Send.Send;
import com.futureWork.Bombay.Send.SendRepo;


@Configuration
public class DatabaseContainer {
	private static final Logger log = LoggerFactory.getLogger(DatabaseContainer.class);
	
	@Bean
	CommandLineRunner initDatabase(SendRepo repo) {
		return args -> {
		      log.info("Preloading " + repo.save(new Send("time#sender#recever#hashcode",(long)124314,(long)3143113,0,"this is a message",new Timestamp(System.currentTimeMillis()))));
		    };
	}
}
