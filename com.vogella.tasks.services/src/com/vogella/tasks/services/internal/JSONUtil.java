package com.vogella.tasks.services.internal;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.vogella.tasks.model.Task;

public class JSONUtil {

	private JSONUtil() {
		// not to initialize
	}

	private static String fileName = "dataFile";

	public static void saveAsGson(List<Task> tasks) {
		final String dir = System.getProperty("user.dir");

		Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
				.create();

		java.lang.reflect.Type type = new TypeToken<List<Task>>() {
		}.getType();
		String json = gson.toJson(tasks, type);

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

	public static List<Task> retrieveSavedData() {
		List<String> readTextFileByLines = readTextFileByLines(fileName);
		if (readTextFileByLines != null && !readTextFileByLines.isEmpty()) {
			Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

			var type = new TypeToken<List<Task>>() {
			}.getType();
			String string = readTextFileByLines.stream().collect(Collectors.joining());

			try {
				List<Task> fromJson = gson.fromJson(string, type);
				fromJson.forEach(Task::restorePropertyChangeListener);
				return fromJson;
			} catch (Exception e) {
				System.err.println("Error reading JSON: " + e.getMessage());
				return new ArrayList<>();
			}
		}
		return new ArrayList<>();
	}

	private static class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
		private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

		@Override
		public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
			return new JsonPrimitive(date.format(formatter));
		}

		@Override
		public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			return LocalDate.parse(json.getAsString(), formatter);
		}
	}
}
