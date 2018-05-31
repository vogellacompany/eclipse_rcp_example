package com.example.e4.rcp.todo.rest.services.internal;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.example.e4.rcp.todo.model.Todo;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TodoRestCallJob extends Job {

	public static final String API_URL = "http://www.mocky.io";

	private Consumer<List<Todo>> todosConsumer;

	public TodoRestCallJob(Consumer<List<Todo>> todosConsumer) {
		super("Fetch Todos from http://www.mocky.io webserver");
		this.todosConsumer = todosConsumer;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {


		Retrofit retrofit = new Retrofit.Builder().baseUrl(API_URL).addConverterFactory(GsonConverterFactory.create())
				.build();

		TodoRestRequest todoRestRequest = retrofit.create(TodoRestRequest.class);

		Call<List<Todo>> todoCall = todoRestRequest.getTodos();

		try {
			List<Todo> contributors = todoCall.execute().body();
			todosConsumer.accept(contributors);
		} catch (IOException e) {
			Bundle bundle = FrameworkUtil.getBundle(getClass());
			return new Status(Status.ERROR, bundle.getSymbolicName(), e.getMessage(), e);
		}

		return Status.OK_STATUS;
	}

}
