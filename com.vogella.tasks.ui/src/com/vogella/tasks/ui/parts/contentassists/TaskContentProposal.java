package com.vogella.tasks.ui.parts.contentassists;

import org.eclipse.jface.fieldassist.ContentProposal;

import com.vogella.tasks.model.Task;

public class TaskContentProposal extends ContentProposal {

	private Task task;

	public TaskContentProposal(String content, Task task) {
		super(content);
		this.task = task;
	}

	public Task getTask() {
		return task;
	}


}
