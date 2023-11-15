package es.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import org.springframework.web.client.RestTemplate;

import es.demo.controller.ClientController;
import es.demo.gui.ClientGUI;

@SpringBootApplication
  public class UserClientApplication implements CommandLineRunner {
 
  private static final Logger log = LoggerFactory.getLogger(UserClientApplication.class);
  
  // Field dependency injection
  @Autowired
  private ClientController clientController;
  
  @Bean
  RestTemplate restTemplate() {
      return new RestTemplate();
  }
  // Spring Boot applications do not have GUI by default (headless = true) - we must disable it  
  static {
      System.getProperties().setProperty("java.awt.headless", "false");  
  }
  
  public static void main(String args[]) {
	  SpringApplication.run(UserClientApplication.class, args);
	  
  }	  
    
  public void run (String...args) throws Exception {
	  
	  log.info("--------------------------------------------------------------------------------");
	  log.info("Starting the GUI ...");
	  log.info("--------------------------------------------------------------------------------");
	  new ClientGUI(clientController);
  }
   
}  
  
 