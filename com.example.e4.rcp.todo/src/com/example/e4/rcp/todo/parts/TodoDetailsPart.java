package com.example.e4.rcp.todo.parts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
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
	private MPart part;

	@Inject
	@DirectTodo(id = 1)
	private java.util.Optional<Todo> todo;

	private WritableValue<Todo> observableTodo = new WritableValue<>();

	private DataBindingContext dbc;

	// pause dirty listener when new Todo selection is set
	private boolean pauseDirtyListener;

	@PostConstruct
	public void createControls(Composite parent, MessagesRegistry messagesRegistry) {

		Label lblSummary = new Label(parent, SWT.NONE);
		// set Label text and register Label text locale changes
		messagesRegistry.register(lblSummary::setText, m -> m.txtSummary);

		txtSummary = new Text(parent, SWT.BORDER);

		Label lblDescription = new Label(parent, SWT.NONE);
		// set Label text and register Label text locale changes
		messagesRegistry.register(lblDescription::setText, m -> m.txtDescription);

		txtDescription = new Text(parent, SWT.BORDER | SWT.MULTI);

		Label lblDueDate = new Label(parent, SWT.NONE);
		// set Label text and register Label text locale changes
		messagesRegistry.register(lblDueDate::setText, m -> m.lblDueDate);

		dateTime = new DateTime(parent, SWT.BORDER);
		new Label(parent, SWT.NONE);

		btnDone = new Button(parent, SWT.CHECK);
		// set Label text and register Label text locale changes
		messagesRegistry.register(btnDone::setText, m -> m.buttonDone);

		bindData();

		updateUserInterface(todo);

		GridLayoutFactory.swtDefaults().numColumns(2).generateLayout(parent);
	}

	@SuppressWarnings("unchecked")
	private void bindData() {
		// this assumes that widget field is called "summary"
		if (txtSummary != null && !txtSummary.isDisposed()) {

			dbc = new DataBindingContext();

			Map<String, IObservableValue<?>> fields = new HashMap<>();
			fields.put(Todo.FIELD_SUMMARY, WidgetProperties.text(SWT.Modify).observe(txtSummary));
			fields.put(Todo.FIELD_DESCRIPTION, WidgetProperties.text(SWT.Modify).observe(txtDescription));
			fields.put(Todo.FIELD_DUEDATE, WidgetProperties.selection().observe(dateTime));
			fields.put(Todo.FIELD_DONE, WidgetProperties.selection().observe(btnDone));
			fields.forEach((k, v) -> dbc.bindValue(v, BeanProperties.value(k).observeDetail(observableTodo)));

			dbc.getBindings().forEach(item -> {
				Binding binding = (Binding) item;
				binding.getTarget().addChangeListener(e -> {
					if (!pauseDirtyListener && part != null) {
						part.setDirty(true);
					}
				});
			});
		}
	}

	@Inject
	public void setTodos(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) List<Todo> todos) {
		if (todos == null || todos.isEmpty()) {
			this.todo = java.util.Optional.empty();
		} else {
			this.todo = java.util.Optional.of(todos.get(0));
		}
		// Remember the todo as field
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
		part.setDirty(false);
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
