package com.example.e4.rcp.todo.model;

import java.util.ArrayList;
import java.util.List;

public class Tag<T> {

	public static String LABEL_FIELD = "label";

	public static String TAGGEDELEMENTS_FIELD = "taggedElements";

	private String label;

	private List<T> taggedElements;

	public Tag(String Label, List<T> taggedElements) {
		label = Label;
		this.taggedElements = new ArrayList<>(taggedElements);
	}
	
	public String getLabel() {
		return label;
	}

	public List<T> getTaggedElements() {
		return taggedElements;
	}

	public void addTaggedElement(T element) {
		this.taggedElements.add(element);
	}

	public void removeTaggedElement(T element) {
		this.taggedElements.remove(element);
	}

}
