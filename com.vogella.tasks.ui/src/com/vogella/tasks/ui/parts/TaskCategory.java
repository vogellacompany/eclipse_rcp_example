package com.vogella.tasks.ui.parts;

import java.time.LocalDate;

import org.eclipse.swt.graphics.RGB;

import com.vogella.tasks.model.Task;

/** Groups tasks by their state for the summary bar of the overview. */
public enum TaskCategory {

	OVERDUE("Overdue", new RGB(0xC0, 0x39, 0x2B)),
	DUE_TODAY("Due today", new RGB(0xF0, 0xB2, 0x29)),
	UPCOMING("Upcoming", new RGB(0x2E, 0x86, 0xC1)),
	DONE("Done", new RGB(0x2E, 0x8B, 0x57));

	private final String label;
	private final RGB color;

	TaskCategory(String label, RGB color) {
		this.label = label;
		this.color = color;
	}

	public String label() {
		return label;
	}

	public RGB color() {
		return color;
	}

	public static TaskCategory of(Task task, LocalDate today) {
		if (task.isDone()) {
			return DONE;
		}
		LocalDate due = task.getDueDate();
		if (due == null || due.isAfter(today)) {
			return UPCOMING;
		}
		return due.isEqual(today) ? DUE_TODAY : OVERDUE;
	}
}
