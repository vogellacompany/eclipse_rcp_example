package com.vogella.tasks.services.internal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vogella.tasks.model.Todo;

public class JSONUtil {

	private JSONUtil() {
		// not to initialize
	}

	private static String fileName = "dataFile";

	public static void saveAsGson(List<Todo> todos) {
		final String dir = System.getProperty("user.dir");
		// Save data as new JSON
		Gson gson = new Gson();
		java.lang.reflect.Type type = new TypeToken<List<Todo>>() {
		}.getType();
		String json = gson.toJson(todos, type);
		try {
			Files.write(Paths.get(dir + File.separator + fileName), json.getBytes(), StandardOpenOption.CREATE);
		} catch (IOException e) {
			// ignore
		}
	}

	public static List<String> readTextFileByLines(String fileName) {
		final String dir = System.getProperty("user.dir");
		try {
			return Files.readAllLines(Paths.get(dir + File.separator + fileName));
		} catch (IOException e) {
			return Collections.emptyList();
		}
	}

	public static List<Todo> retrieveSavedTodos() {
		List<String> readTextFileByLines = readTextFileByLines(fileName);
		if (readTextFileByLines != null && !readTextFileByLines.isEmpty()) {
			var gson = new Gson();
			var type = new TypeToken<List<Todo>>() {
			}.getType();
			String string = readTextFileByLines.stream().map(String::valueOf).collect(Collectors.joining());
			// Convert back to Java
			List<Todo> fromJson = gson.fromJson(string, type);
			fromJson.forEach(Todo::restorePropertyChangeListener);
			return fromJson;
		}
		return new ArrayList<>();
	}
}
