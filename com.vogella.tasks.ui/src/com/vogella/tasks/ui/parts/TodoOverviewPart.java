package com.vogella.tasks.ui.parts;

import static org.eclipse.jface.layout.GridLayoutFactory.fillDefaults;
import static org.eclipse.jface.widgets.ButtonFactory.newButton;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import com.vogella.tasks.model.Task;
import com.vogella.tasks.model.TaskService;

public class TodoOverviewPart {

	@Inject
	TaskService taskService;

	private WritableList<Task> writableList;

	private TableViewer viewer;

	@PostConstruct
	public void createControls(Composite parent) {
		fillDefaults().numColumns(1).applyTo(parent);

		newButton(SWT.PUSH).text("Load Data").onSelect(e -> update()).create(parent);

		viewer = new TableViewer(parent, SWT.MULTI | SWT.FULL_SELECTION);
		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// create column for the summary property
		TableViewerColumn colSummary = new TableViewerColumn(viewer, SWT.NONE);
		colSummary.getColumn().setWidth(100);
		colSummary.getColumn().setText("Summary");

		// create column for description property
		TableViewerColumn colDescription = new TableViewerColumn(viewer, SWT.NONE);

		colDescription.getColumn().setWidth(200);
		colDescription.getColumn().setText("Description");

		// use data binding to bind the viewer
		writableList = new WritableList<>();
		// fill the writable list, when Consumer callback is called. Databinding
		// will do the rest once the list is filled
		taskService.consume(writableList::addAll);
		ViewerSupport.bind(viewer, writableList, BeanProperties.values(Task.FIELD_SUMMARY, Task.FIELD_DESCRIPTION));

	}

	public void updateViewer(List<Task> list) {
		if (viewer != null) {
			writableList.clear();
			writableList.addAll(list);
		}
	}

	@Focus
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	private void update() {
		updateViewer(taskService.getAll());
    }
}