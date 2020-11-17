package com.vogella.tasks.ui.parts.contentassists;

import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;

import com.vogella.tasks.model.Task;

public class TaskContentProposalProvider implements IContentProposalProvider {

	private List<Task> tasks;
	public TaskContentProposalProvider(List<Task> tasks) {
		this.tasks = tasks;
	}

	@Override
	public IContentProposal[] getProposals(String contents, int position) {
		String substring = contents.substring(0, position);
		Pattern pattern = Pattern.compile(substring, Pattern.LITERAL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		IContentProposal[] filteredProposals = tasks.stream()
				.filter(task -> task.getSummary().length() >= substring.length()
						&& pattern.matcher(task.getSummary()).find())
				.map(task -> new TaskContentProposal(task.getSummary(), task)).toArray(TaskContentProposal[]::new);
		return filteredProposals.length == 0 ? null : filteredProposals;
	}

	// Simpler version of the above
//	@Override
//	public IContentProposal[] getProposals(String contents, int position) {
//
//		for (Task task : tasks) {
//			if (task.getSummary().toLowerCase().startsWith(contents.toLowerCase())) {
//				proposals.add(new TaskContentProposal(task.getSummary(), task));
//			}
//		}
//		return proposals.toArray(new TaskContentProposal[0]);
//	}

	public void setProposals(List<Task> tasks) {
		this.tasks = tasks;
	}
}
