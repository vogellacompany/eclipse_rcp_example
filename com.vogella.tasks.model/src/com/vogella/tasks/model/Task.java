package com.vogella.tasks.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;
import java.util.Objects;

public class Task {

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
		changes.firePropertyChange(FIELD_SUMMARY, this.summary, this.summary = summary);
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

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
		changes.firePropertyChange(FIELD_DUEDATE, this.dueDate, this.dueDate = dueDate);
    }

    public long getId() {
        return id;
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
		Task other = (Task) obj;
		return id == other.id;
	}

	@Override
    public String toString() {
        return "Task [id=" + id + ", summary=" + summary + "]";
    }

    public Task copy() {
        return new Task(this.id, this.summary, this.description, this.done, this.dueDate);
    }

	public void addPropertyChangeListener(PropertyChangeListener l) {
		changes.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		changes.removePropertyChangeListener(l);
	}

	/**
	 * Only need for JSON serialization and de-serialization
	 */
	public void restorePropertyChangeListener() {
		if (Objects.isNull(changes)) {
			changes = new PropertyChangeSupport(this);
		}
	}
}