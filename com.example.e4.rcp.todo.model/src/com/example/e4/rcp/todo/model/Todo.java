package com.example.e4.rcp.todo.model;

import java.util.Date;

import org.eclipse.core.databinding.observable.value.WritableValue;

public class Todo {
	
	public static final String FIELD_ID = "id";
	public static final String FIELD_SUMMARY = "summary";
	public static final String FIELD_DESCRIPTION = "description";
	public static final String FIELD_DONE = "done";
	public static final String FIELD_DUEDATE = "dueDate";
	
	public final long id;
	private WritableValue<String> summary;
	private WritableValue<String> description;
	private WritableValue<Boolean> done;
	private WritableValue<Date> dueDate;
	
	public Todo(long i) {
		this(i, "", "", false, null);
	}

	public Todo(long i, String summary, String description, boolean b, Date date) {
		this.id = i;
		this.summary = new WritableValue<>(summary, String.class);
		this.description = new WritableValue<>(description, String.class);
		this.done = new WritableValue<>(b, Boolean.class);
		this.dueDate = new WritableValue<>(date, Date.class);  
	}

	

	public long getId() {
		return id;
	}
	
	public String getSummary() {
		return summary.getValue();
	}

	public void setSummary(String summary) {
		this.summary.setValue(summary);
	}

	public String getDescription() {
		return description.getValue();
	}

	public void setDescription(String description) {
		this.description.setValue(description);
	}

	public boolean isDone() {
		return done.getValue();
	}

	public void setDone(boolean isDone) {
		this.done.setValue(isDone);
	}

	public Date getDueDate() {
		return dueDate.getValue();
	}

	public void setDueDate(Date dueDate) {
		this.dueDate.setValue(dueDate);
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

	@Override
	public String toString() {
		return "Todo [id=" + id + ", summary=" + summary + "]";
	}

	public Todo copy() {
		return new Todo(id, summary.getValue(), description.getValue(), done.getValue(), dueDate.getValue());
	}
}
