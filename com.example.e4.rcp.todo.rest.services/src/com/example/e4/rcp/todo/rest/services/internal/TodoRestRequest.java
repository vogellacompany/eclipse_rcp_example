package com.example.e4.rcp.todo.rest.services.internal;

import java.util.List;

import com.example.e4.rcp.todo.model.Todo;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TodoRestRequest {
	@GET
	("/v2/56cf6647120000e901b4cf95")
	Call<List<Todo>> getTodos();
}
