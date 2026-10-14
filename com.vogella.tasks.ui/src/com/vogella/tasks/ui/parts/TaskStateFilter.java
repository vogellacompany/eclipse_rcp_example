package com.vogella.tasks.ui.parts;

import java.time.LocalDate;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.vogella.tasks.model.Task;

/** Shows only the tasks with the selected state: all, open, done or overdue. */
public class TaskStateFilter extends ViewerFilter {

	public static final String CONTEXT_KEY = "com.vogella.tasks.ui.taskfilter";

	private String state = "all";

	public void setState(String state) {
		this.state = state == null ? "all" : state;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		Task task = (Task) element;
		LocalDate due = task.getDueDate();
		return switch (state) {
		case "open" -> !task.isDone();
		case "done" -> task.isDone();
		case "overdue" -> !task.isDone() && due != null && due.isBefore(LocalDate.now());
		default -> true;
		};
	}
}
