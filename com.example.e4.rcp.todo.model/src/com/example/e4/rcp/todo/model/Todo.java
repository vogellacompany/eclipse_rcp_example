package com.example.e4.rcp.todo.model;

import java.util.Date;

public class Todo {
	private long id;
	String summary = "";
	String description = "";
	boolean done = false;
	Date dueDate;
	

	public Todo() {
		super();
	}

	public Todo(long id, String summary, String description, boolean done,
			Date dueDate) {
		super();
		this.id = id;
		this.summary = summary;
		this.description = description;
		this.done = done;
		this.dueDate = dueDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Todo other = (Todo) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	@Override
	public String toString() {
		return "Todo [id=" + id + ", summary=" + summary + "]";
	}

	public Todo copy() {
		return new Todo(this.id, this.summary, this.description, this.done,
				this.dueDate);
	}

}
