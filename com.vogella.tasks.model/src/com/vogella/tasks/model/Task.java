package com.vogella.tasks.model;

import java.time.LocalDate;

public class Task {

	public static final String FIELD_ID = "id";
	public static final String FIELD_SUMMARY = "summary";
	public static final String FIELD_DESCRIPTION = "description";
	public static final String FIELD_DONE = "done";
	public static final String FIELD_DUEDATE = "dueDate";

    private final long id;
    private String summary = "";
    private String description = "";
    private boolean done = false;
    private LocalDate dueDate = LocalDate.now();

    public Task(long id, String summary, String description, boolean done, LocalDate dueDate) {
        this.id = id;
        this.summary = summary;
        this.description = description;
        this.done = done;
        this.dueDate = dueDate;
    }

    public Task(long id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Task [id=" + id + ", summary=" + summary + "]";
    }

    public Task copy() {
        return new Task(this.id, this.summary, this.description, this.done, this.dueDate);
    }

}