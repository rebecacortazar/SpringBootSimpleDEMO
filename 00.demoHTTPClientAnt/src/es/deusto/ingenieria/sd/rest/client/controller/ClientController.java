package es.deusto.ingenieria.sd.rest.client.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;

import com.google.gson.Gson;

public class ClientController {
	
	private static String BASE_URL;
	private static Gson gson = new Gson();
	
	public record User(long id, String firstName, String lastName, String email) {		
	};
	
	public ClientController (String connectionURL) {
		BASE_URL=connectionURL;
	}
	
	public String getAllUsers() {
		System.out.format("- Getting all users: %s:/user/all ...", BASE_URL);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "user/all"))
                .build();

        StringBuffer buffer = new StringBuffer();
        
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {                
            	User[] users = gson.fromJson(response.body(), User[].class);
        		
        		if (users != null && users.length > 0) {
        			Arrays.asList(users).forEach(user -> buffer.append(" - " + user.toString() + "\n"));
        		} else {
        			buffer.append(" - No users found");
        		}
            } else {
            	buffer.append(String.format("Error: %d", response.statusCode()));
            }
        } catch (Exception e) {
        	System.out.format("- ERROR Getting all users: %s:/user/all [%s]", BASE_URL, e.getMessage());
        }

		return buffer.toString();
	}

	public String getUserById() {
		System.out.format("Getting user details by id(1): %suser/details/{id} ...", BASE_URL);
		
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "user/details/1"))
                .build();

        StringBuffer buffer = new StringBuffer();
        
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {                     	
            	User user = gson.fromJson(response.body(), User.class);
            	
            	if (user != null) {            	
            		buffer.append(String.format(" - %s", user.toString()));
            	} else {
            		buffer.append(" - User not found");
            	}
            } else {
            	buffer.append(String.format("- Error: %d", response.statusCode()));
            }

        } catch (Exception e) {
        	System.out.format("- ERROR Getting user details by id(1): %suser/details/{id} [%s]", BASE_URL, e.getMessage());
        }
				
        return buffer.toString();
	}

	public String getUserByEmail() {		
		System.out.format("Getting user details by email(rebeca.cortazar@deusto.es): %suser/email/{email} ...", BASE_URL);
		
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "user/email/rebeca.cortazar@deusto.es"))
                .build();

        StringBuffer buffer = new StringBuffer();
        
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {                     	
            	User user = gson.fromJson(response.body(), User.class);
            	
            	if (user != null) {            	
            		buffer.append(String.format(" - %s", user.toString()));
            	} else {
            		buffer.append(" - User not found");
            	}
            } else {
            	buffer.append(String.format("- Error: %d", response.statusCode()));
            }

        } catch (Exception e) {
        	System.out.format("- ERROR Getting user details by email(rebeca.cortazar@deusto.es): %suser/details/{id} [%s]", BASE_URL, e.getMessage());
        }
				
        return buffer.toString();
	}

	public String addUser() {
		System.out.format("Creating new user: %suser/create ...", BASE_URL);
						
        HttpClient client = HttpClient.newHttpClient();

        User user = new User(20, "Jon", "Jones", "jj@deusto.es");
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "user/create"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(user)))
                .build();

        StringBuffer buffer = new StringBuffer();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());            
            
            buffer.append(String.format("- Status code: %d", response.statusCode()));
        } catch (Exception e) {
        	System.out.format("- ERROR Creating new user: %suser/create [%s]", BASE_URL, e.getMessage());
        }
		
        return buffer.toString();
	}

	public String updateUser() {
		System.out.format("Updating a user (id=3): %s/user/update/{id} ...", BASE_URL);
		
        HttpClient client = HttpClient.newHttpClient();

        User user = new User(3, "Alan", "Alanson", "aa@gmail.com");
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "user/update/3"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(user)))
                .build();
        
        StringBuffer buffer = new StringBuffer();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());            
            
            buffer.append(String.format("- Status code: %d", response.statusCode()));
        } catch (Exception e) {
        	System.out.format("- ERROR Updating a user (id=3): %s/user/update/{id} [%s]", BASE_URL, e.getMessage());
        }
		
        return buffer.toString();
	}

	public String deleteUser() {
		System.out.format("Deleting a user (id=1): %s/user/delete/{id} ...", BASE_URL);
		
		HttpClient client = HttpClient.newHttpClient();
		
		HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "user/delete/11"))
                .DELETE()
                .build();
		
        StringBuffer buffer = new StringBuffer();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());            
            
            buffer.append(String.format("- Status code: %d", response.statusCode()));
        } catch (Exception e) {
        	System.out.format("- ERROR Deleting a user (id=1): %s/user/delete/{id} [%s]", BASE_URL, e.getMessage());
        }
		
        return buffer.toString();
	}

	public String deleteUserAll() {
		System.out.format("Deleting all users: %s:/user/delete/all ...", BASE_URL);
		
		HttpClient client = HttpClient.newHttpClient();
		
		HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "user/delete/all"))
                .DELETE()
                .build();
		
        StringBuffer buffer = new StringBuffer();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());            
            
            buffer.append(String.format("- Status code: %d", response.statusCode()));
        } catch (Exception e) {
        	System.out.format("- ERROR Deleting all users: %s:/user/delete/all [%s]", BASE_URL, e.getMessage());
        }
		
        return buffer.toString();
	}
}