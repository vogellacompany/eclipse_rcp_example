package com.example.e4.rcp.todo.restservices;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jsonp.JsonProcessingFeature;

import com.example.e4.rcp.todo.model.ITodoService;
import com.example.e4.rcp.todo.model.Todo;
import com.example.e4.rcp.todo.model.TodoRestUriConstants;

public class RestTodoServiceImpl implements ITodoService {

	public static final String SERVER_URI = "http://localhost:8080/todo";

	@Override
	public List<Todo> getTodos() {
		try {

			Client client = ClientBuilder.newClient(new ClientConfig()
					.register(JsonProcessingFeature.class));

			WebTarget webTarget = client.target(SERVER_URI).path(
					TodoRestUriConstants.GET_ALL_TODOS);

			String response = webTarget
					.request(MediaType.APPLICATION_JSON_TYPE).get(String.class);

			System.out.println("Server response .... \n");
			System.out.println(response);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	@Override
	public boolean saveTodo(Todo todo) {

		return true;
	}

	@Override
	public Todo getTodo(long id) {
		return null;
	}

	@Override
	public boolean deleteTodo(long id) {
		return false;
	}

}
