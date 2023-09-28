package com.vogella.tasks.ui.parts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.widgets.LabelFactory;
import org.eclipse.jface.widgets.WidgetFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Text;

import com.vogella.tasks.model.Task;
import com.vogella.tasks.model.TaskService;

public class TodoDetailsPart {
	private Text txtSummary;
	private Text txtDescription;
	private DateTime dateTime;
	private Button btnDone;

	// define a new field
	private java.util.Optional<Task> task = java.util.Optional.empty();

	// observable placeholder for a task
	private WritableValue<Task> observableTodo = new WritableValue<>();
	private DataBindingContext dbc;
	@Inject
	private MPart part;

	// pause dirty listener when new selection is set
	private boolean pauseDirtyListener;

	@PostConstruct
	public void createControls(Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		GridDataFactory gdFactory = GridDataFactory.fillDefaults();
		LabelFactory labelFactory = LabelFactory.newLabel(SWT.NONE);

		labelFactory.text("Summary").create(parent);

		txtSummary = WidgetFactory.text(SWT.BORDER).layoutData(gdFactory.grab(true, false).create())//
				.create(parent);

		labelFactory.text("Description").create(parent);
		txtDescription = WidgetFactory.text(SWT.BORDER | SWT.MULTI | SWT.V_SCROLL)
				.layoutData(gdFactory.align(SWT.FILL, SWT.FILL).grab(true, true).create()).create(parent);

		labelFactory.text("Due Date").create(parent);

		// Factory planned for 2020-12 release
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=567110

		dateTime = new DateTime(parent, SWT.BORDER);
		dateTime.setLayoutData(gdFactory.align(SWT.FILL, SWT.CENTER).grab(false, false).create());

		labelFactory.text("").create(parent);

		btnDone = WidgetFactory.button(SWT.CHECK).text("Done").create(parent);

		bindData();
		updateUserInterface(task);
	}

	private void bindData() {
		// this assumes that widget field is called "summary"
		if (txtSummary != null && !txtSummary.isDisposed()) {

			dbc = new DataBindingContext();

			Map<String, IObservableValue<?>> fields = new HashMap<>();
			fields.put(Task.FIELD_SUMMARY, WidgetProperties.text(SWT.Modify).observe(txtSummary));
			fields.put(Task.FIELD_DESCRIPTION, WidgetProperties.text(SWT.Modify).observe(txtDescription));
			fields.put(Task.FIELD_DUEDATE, WidgetProperties.localDateSelection().observe(dateTime));
			fields.put(Task.FIELD_DONE, WidgetProperties.buttonSelection().observe(btnDone));
			fields.forEach((k, v) -> dbc.bindValue(v, BeanProperties.value(k).observeDetail(observableTodo)));

			dbc.getBindings().forEach(item -> {
				Binding binding = item;
				binding.getTarget().addChangeListener(e -> {
					if (!pauseDirtyListener && part != null) {
						part.setDirty(true);
					}
				});
			});
		}
	}

	// Add the following new methods to your code

	@Inject
	public void setTasks(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) List<Task> tasks) {
		if (tasks == null || tasks.isEmpty() || tasks.get(0) == null) {
			this.task = java.util.Optional.empty();
		} else {
			this.task = java.util.Optional.of(tasks.get(0));
		}
		// Remember the task as field update the user interface
		updateUserInterface(this.task);
	}

	// allows to disable/ enable the user interface fields
	// if no task is set
	private void enableUserInterface(boolean enabled) {
		if (txtSummary != null && !txtSummary.isDisposed()) {
			txtSummary.setEnabled(enabled);
			txtDescription.setEnabled(enabled);
			dateTime.setEnabled(enabled);
			btnDone.setEnabled(enabled);
		}
	}

	private void updateUserInterface(java.util.Optional<Task> task) {
		if (!task.isPresent()) {
			enableUserInterface(false);
			return; // nothing left to do
		}

		enableUserInterface(true);
		// the following check ensures that the user interface is available,
		// it assumes that you have a text widget called "txtSummary"
		if (txtSummary != null && !txtSummary.isDisposed()) {
			pauseDirtyListener = true;
			this.observableTodo.setValue(task.get());
			pauseDirtyListener = false; //
		}
	}

	@Persist
	public void save(TaskService todoService) {
		task.ifPresent(todoService::update);
		if (part != null) {
			part.setDirty(false);
		}
	}

	@Focus
	public void setFocus() {
		txtSummary.setFocus();
	}

	@PreDestroy
	public void dispose() {
		dbc.dispose();
    }

}