package com.vogella.tasks.ui.parts;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.vogella.tasks.model.Task;
import com.vogella.tasks.model.TaskService;



public class TodoOverviewPart {

	// use field injection for the service
	@Inject
	ESelectionService selectionService;

	@PostConstruct
	public void createControls(Composite parent, TaskService taskService) {
		taskService.consume(todos -> {
            System.out.println("Number of Todo objects " + todos.size());
        });
		var tasks = new ArrayList<Task>();
		taskService.consume(tasks::addAll);
		var todo = tasks.get(0);
		todo.setDescription("Hello");
		taskService.update(todo);
		TableViewer viewer = new TableViewer(parent, SWT.FULL_SELECTION);
		// viewer is a JFace Viewer
		viewer.addSelectionChangedListener(event -> {
			IStructuredSelection selection = viewer.getStructuredSelection();
			selectionService.setSelection(selection.getFirstElement());
		});
    }
}