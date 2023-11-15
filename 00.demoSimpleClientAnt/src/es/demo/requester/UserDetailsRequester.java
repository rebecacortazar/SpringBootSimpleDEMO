package es.demo.requester;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import es.demo.pojo.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SpringBootApplication
@Service
public class UserDetailsRequester implements IUserRequest {
	private static final Logger log = LoggerFactory.getLogger(UserDetailsRequester.class);
		
	private RestTemplate restTemplate;
	private static UserDetailsRequester requester;
	
	@Bean
	RestTemplate restTemplate() {
	      return new RestTemplate();
	}	
	
	// Host and port NOT hard-coded: Defined in application.properties
	@Value("${spring.server.url}")
	private String serverURL;
	
	@Value("${server.port}")
	private int serverPort;
	
	public static void start() {
		SpringApplication.run(UserDetailsRequester.class);	
	}
		
	public UserDetailsRequester () { }
	
	@Autowired
	public void setRequester(UserDetailsRequester r) {
		 requester = r;
	 }
	public static UserDetailsRequester getRequester() {
		return requester;
	}
	@Autowired
	public void setRestTemplate (RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	/* *
	 * Interface implementation methods
	 * */
	
	public String getListUsers() {
		//Example of parameterized logging
		  log.info("This is the endpoint: {}:{}/user/all - Getting all users...", serverURL, String.valueOf(serverPort));
		  User[] users = restTemplate.getForObject(serverURL + ":" + String.valueOf(serverPort) + "/user/all", User[].class);
		  log.info("user/all - All users info ...");
		  
		  if (users != null) {
			  	for (User u: users) {
			  		log.info(u.toString());
			  	}
		  }
		  return "getListUsers -  all users info on console (if not null) ";
	  }
	
	  public String getUserById() {
			 log.info("This is the endpoint: " + serverURL + ":" + String.valueOf(serverPort) + "/user/details/{id}"); 
			 User user = restTemplate.getForObject(serverURL + ":" + String.valueOf(serverPort) +
					 "/user/details/{id}", User.class, Map.of("id", "1"));
			 log.info("user/details/{id} - This is User: {}, name: {}", user.getId(), user.getFirstName());
			 return "getUserById -  name: " + user.getFirstName();
		  }	 
	  
	  public String getUserByEmail() {
			 log.info("This is the endpoint: " + serverURL + ":" + String.valueOf(serverPort) + "/user/email/{email}"); 
			 User user = restTemplate.getForObject(serverURL + ":" + String.valueOf(serverPort) + "/user/email/{email}", User.class, Map.of("email", "rebeca.cortazar@deusto.es"));
			 log.info("/user/email/{email} - This is User: " + user.getId() + "name: " + user.getFirstName());
			 return "getUserByEmail -  " + user.getFirstName()+" "+ user.getLastName();
	  }	 
	  
	  public String sendEmailUser() {
			// Special use for GET  -  Find a user and given their id, send an email in the server
		  	// Testing case with user 1 and rebeca.cortazar@deusto.es OR rebecacortazar@gmail.com
			 User user = restTemplate.getForObject(serverURL + ":" + String.valueOf(serverPort) + "/user/details/{id}", User.class, Map.of("id", "1"));  
			 log.info("Endpoint: /user/{id}/sendEmail - Sending an email to user: " + user.getEmail());
			 
			 String response = restTemplate.getForObject(serverURL + ":" + String.valueOf(serverPort) + "/user/{id}/sendEmail", String.class, Map.of("id", "1"));
			 log.info("Response from the USER SERVER: " + response);
			 return response;
	  }
	  
	  public String addUser() {
	  	  // POST for creating a new user - email must be unique
		  User newUser = new User("Iker", "Gomez", "ikg@deusto.es");
		  		 
		  log.info ("Endpoint: user/create - Creating new users");
		  String response = restTemplate.postForObject(serverURL + ":" + String.valueOf(serverPort) +"/user/create", newUser, String.class);
		  log.info("The response from the server is: " + response);
		  
		  return "addUser - new user created";
	  }
	  
	  public String updateUser() {
	    // PUT - Change the name of a user
		  log.info("Endpoint: user/update/{id} - Change email of one User - rebeca.cortazar@deusto.es TO rebecacortazar@gmail.com ");
		  
		  User userS = restTemplate.getForObject(serverURL + ":" + String.valueOf(serverPort) +"/user/details/{id}", User.class, Map.of("id", "1"));
		  log.info("User for update: " + userS.getFirstName());
		  
		  User userP = new User (userS.getFirstName(), userS.getLastName(), "rebecacortazar@gmail.com");
		  restTemplate.put(serverURL + ":" + String.valueOf(serverPort) +"/user/update/{id}", userP, Map.of("id", "1"));
		  
		  return "update user- getting user 1 and changing the email";
	  }
		 	  
	  public String deleteUser() {
		    // DELETE  - Remove User 3
			  log.info("Endpoint: user/delete/{id} - Removal of User 3");

			  User userS = restTemplate.getForObject(serverURL + ":" + String.valueOf(serverPort) + "/user/details/{id}", User.class, Map.of("id", "3"));
			  log.info("User to delete: id " + userS.getId() + "; email: " + userS.getEmail());
			  // The delete() in RestTemplate does not have a return; in case of need, exchange() can be programmed	  
			  restTemplate.delete(serverURL + ":" + String.valueOf(serverPort) + "/user/delete/{id}", Map.of("id", "3"));
			  return "delete User - does not have a return";
	  }
	  
	  public String deleteUserAll() {
		    // DELETE  - Remove All Users from the DB
			  log.info("Endpoint: user/delete/all - Removal of ALL Users");

			 // The delete() in RestTemplate does not have a return	  
			  restTemplate.delete(serverURL + ":" + String.valueOf(serverPort) + "/user/delete/all");
			  return "delete all users - this endpoint does not have a return";
	  }
}
