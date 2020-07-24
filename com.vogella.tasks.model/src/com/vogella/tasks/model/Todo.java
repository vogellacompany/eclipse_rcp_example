package com.vogella.tasks.model;

import static java.util.Objects.isNull;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;
import java.util.Objects;

public class Todo {

	private transient PropertyChangeSupport changes = new PropertyChangeSupport(this);

	public static final String FIELD_ID = "id";
	public static final String FIELD_SUMMARY = "summary";
	public static final String FIELD_DESCRIPTION = "description";
	public static final String FIELD_DONE = "done";
	public static final String FIELD_DUEDATE = "dueDate";

	private final long id;
	private String summary = "";
	private String description = "";
	private boolean done = false;
	private Date dueDate = new Date();

	public Todo(long id, String summary, String description, boolean done, Date dueDate) {
		this.id = id;
		this.summary = summary;
		this.description = description;
		this.done = done;
		this.dueDate = dueDate;
	}

	public Todo(long id) {
		this.id = id;
	}

	public String getSummary() {
		return summary;
	}

	/**
	 * Only need for JSON serialization and de-serialization
	 */
	public void restorePropertyChangeListener() {
		if (isNull(changes)) {
			changes = new PropertyChangeSupport(this);
		}
	}

	public void setSummary(String summary) {
        changes.firePropertyChange(FIELD_SUMMARY, this.summary,
                this.summary = summary);
    }

	public String getDescription() {
		return description;
	}
	   public void setDescription(String description) {
	        changes.firePropertyChange(FIELD_DESCRIPTION, this.description, this.description = description);
	    }


	public boolean isDone() {
		return done;
	}

	public void setDone(boolean isDone) {
		changes.firePropertyChange(FIELD_DONE, this.done, this.done = isDone);
	}

	public Date getDueDate() {
		return new Date(dueDate.getTime());
	}


	public void setDueDate(Date dueDate) {
		changes.firePropertyChange(FIELD_DUEDATE, this.dueDate, this.dueDate = new Date(dueDate.getTime()));
	}

	public long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Todo [id=" + id + ", summary=" + summary + "] + , description=" + description + "] ";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
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
		return id == other.id;
	}

	public Todo copy() {
		return new Todo(this.id, this.summary, this.description, this.done, getDueDate());
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		changes.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		changes.removePropertyChangeListener(l);
	}

}