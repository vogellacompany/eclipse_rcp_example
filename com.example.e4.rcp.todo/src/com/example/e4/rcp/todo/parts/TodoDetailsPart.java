package com.example.e4.rcp.todo.parts;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.example.e4.rcp.todo.i18n.MessagesRegistry;
import com.example.e4.rcp.todo.model.ITodoService;
import com.example.e4.rcp.todo.model.Todo;
import com.example.e4.rcp.todo.ownannotation.DirectTodo;

public class TodoDetailsPart {

	private Text txtSummary;
	private Text txtDescription;
	private Button btnDone;
	private DateTime dateTime;
	
	@Inject
	private MDirtyable dirtyable;

	@Inject
	@DirectTodo(id = 1)
	private java.util.Optional<Todo> todo;
	
	private WritableValue<Todo> observableTodo = new WritableValue<>();

	private DataBindingContext dbc;
	
	// pause dirty listener when new Todo selection is set
	private boolean pauseDirtyListener;
	
	@PostConstruct
	public void createControls(Composite parent, MessagesRegistry messagesRegistry) {

		GridLayout gl_parent = new GridLayout(2, false);
		gl_parent.marginRight = 10;
		gl_parent.marginLeft = 10;
		gl_parent.horizontalSpacing = 10;
		gl_parent.marginWidth = 0;
		parent.setLayout(gl_parent);

		Label lblSummary = new Label(parent, SWT.NONE);
		// set Label text and register Label text locale changes
		messagesRegistry.register(lblSummary::setText, m -> m.txtSummary);

		txtSummary = new Text(parent, SWT.BORDER);
		txtSummary.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		Label lblDescription = new Label(parent, SWT.NONE);
		// set Label text and register Label text locale changes
		messagesRegistry.register(lblDescription::setText, m -> m.txtDescription);

		txtDescription = new Text(parent, SWT.BORDER | SWT.MULTI);
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.heightHint = 122;
		txtDescription.setLayoutData(gd);

		Label lblDueDate = new Label(parent, SWT.NONE);
		// set Label text and register Label text locale changes
		messagesRegistry.register(lblDueDate::setText, m -> m.lblDueDate);

		dateTime = new DateTime(parent, SWT.BORDER);
		dateTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(parent, SWT.NONE);

		btnDone = new Button(parent, SWT.CHECK);
		// set Label text and register Label text locale changes
		messagesRegistry.register(btnDone::setText, m -> m.buttonDone);
		
		bindData();

		updateUserInterface(todo);
	}

	@SuppressWarnings("unchecked")
	private void bindData() {
		// this assumes that widget field is called "summary"
		if (txtSummary != null && !txtSummary.isDisposed()) {

			dbc = new DataBindingContext();

			IObservableValue<String> txtSummaryTarget = WidgetProperties.text(SWT.Modify).observe(txtSummary);
			IObservableValue<String> observeSummary = BeanProperties.value(Todo.FIELD_SUMMARY).observeDetail(observableTodo);
			dbc.bindValue(txtSummaryTarget, observeSummary);
			
			IObservableValue<String> txtDescriptionTarget = WidgetProperties.text(SWT.Modify).observe(txtDescription);
			IObservableValue<String> observeDescription = BeanProperties.value(Todo.FIELD_DESCRIPTION).observeDetail(observableTodo);
			dbc.bindValue(txtDescriptionTarget, observeDescription);
			
			IObservableValue<Boolean> booleanTarget = WidgetProperties.selection().observe(btnDone);
			IObservableValue<Boolean> observeDone = BeanProperties.value(Todo.FIELD_DONE).observeDetail(observableTodo);
			dbc.bindValue(booleanTarget, observeDone);

			IObservableValue<Date> observeSelectionDateTimeObserveWidget = WidgetProperties.selection()
					.observe(dateTime);
			IObservableValue<Date> observeDueDate = BeanProperties.value(Todo.FIELD_DUEDATE).observeDetail(observableTodo);
			dbc.bindValue(observeSelectionDateTimeObserveWidget, observeDueDate);
			
			dbc.getBindings().forEach(item -> {
				Binding binding = (Binding) item;
				binding.getTarget().addChangeListener(e -> {
					if(!pauseDirtyListener) {
						dirtyable.setDirty(true);
					}
				});
			});
		}
	}

	@Inject
	public void setTodo(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Todo todo) {
		// Remember the todo as field
		this.todo = java.util.Optional.ofNullable(todo);
		// update the user interface
		updateUserInterface(this.todo);
	}

	// allows to disable/ enable the user interface fields
	// if no todo is et
	private void enableUserInterface(boolean enabled) {
		if (txtSummary != null && !txtSummary.isDisposed()) {
			txtSummary.setEnabled(enabled);
			txtDescription.setEnabled(enabled);
			dateTime.setEnabled(enabled);
			btnDone.setEnabled(enabled);
		}
	}

	private void updateUserInterface(java.util.Optional<Todo> todo) {

		// check if Todo is present
		if (todo.isPresent()) {
			enableUserInterface(true);
		} else {
			enableUserInterface(false);
			return;
		}

		// Check if the user interface is available
		// assume you have a field called "summary"
		// for a widget
		if (txtSummary != null && !txtSummary.isDisposed()) {
			pauseDirtyListener = true;
			this.observableTodo.setValue(todo.get());
			pauseDirtyListener = false;
		}
	}
	
	@Persist
	public void save(ITodoService todoService) {
		todo.ifPresent(t -> {
			todoService.saveTodo(t);
		});
		dirtyable.setDirty(false);
	}

	@Focus
	public void onFocus() {
		txtSummary.setFocus();
	}
	
	@PreDestroy
	public void dispose() {
		dbc.dispose();
	}
}
