package com.example.e4.rcp.todo.parts;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.example.e4.rcp.todo.events.MyEventConstants;
import com.example.e4.rcp.todo.model.ITodoService;
import com.example.e4.rcp.todo.model.Todo;

public class EditorPart {

	@Inject
	private ECommandService commandService;

	@Inject
	private EHandlerService handlerService;

	@Inject
	private MPart part;

	private Text txtSummary;
	private Text txtDescription;
	private Button btnDone;
	private DateTime dateTime;

	private java.util.Optional<Todo> todo;

	private DataBindingContext dbc;

	@PostConstruct
	public void createControls(Composite parent, ITodoService todoService) {
		// extract the id of the todo item
		String string = part.getPersistedState().get(Todo.FIELD_ID);
		Long idOfTodo = Long.valueOf(string);

		GridLayout gl_parent = new GridLayout(2, false);
		gl_parent.marginRight = 10;
		gl_parent.marginLeft = 10;
		gl_parent.horizontalSpacing = 10;
		gl_parent.marginWidth = 0;
		parent.setLayout(gl_parent);

		// retrieve the todo based on the persistedState
		todo = todoService.getTodo(idOfTodo);

		if (todo.isPresent()) {

			Label lblSummary = new Label(parent, SWT.NONE);
			lblSummary.setText("Summary");

			txtSummary = new Text(parent, SWT.BORDER);
			txtSummary.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

			Label lblDescription = new Label(parent, SWT.NONE);
			lblDescription.setText("Description");

			txtDescription = new Text(parent, SWT.BORDER | SWT.MULTI);
			GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
			gd.heightHint = 122;
			txtDescription.setLayoutData(gd);

			Label lblNewLabel = new Label(parent, SWT.NONE);
			lblNewLabel.setText("Due Date");

			dateTime = new DateTime(parent, SWT.BORDER);
			dateTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			new Label(parent, SWT.NONE);

			btnDone = new Button(parent, SWT.CHECK);
			btnDone.setText("Done");

			Button button = new Button(parent, SWT.PUSH);
			button.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
			button.setText("Save");
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					ParameterizedCommand command = commandService.createCommand("org.eclipse.ui.file.saveAll", null);
					handlerService.executeHandler(command);
				}
			});

			bindData(todo.get());
		} else {
			Label label = new Label(parent, SWT.NONE);
			label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
			label.setText("No suitable todo found!");
		}
	}

	@Persist
	public void save(ITodoService todoService) {
		todo.ifPresent(todo -> todoService.saveTodo(todo));
		part.setDirty(false);
	}

	@SuppressWarnings("unchecked")
	private void bindData(Todo todo) {

		dbc = new DataBindingContext();

		IObservableValue<String> txtSummaryTarget = WidgetProperties.text(SWT.Modify).observe(txtSummary);
		IObservableValue<String> observeSummary = BeanProperties.value(Todo.FIELD_SUMMARY).observe(todo);
		dbc.bindValue(txtSummaryTarget, observeSummary);

		IObservableValue<String> txtDescriptionTarget = WidgetProperties.text(SWT.Modify).observe(txtDescription);
		IObservableValue<String> observeDescription = BeanProperties.value(Todo.FIELD_DESCRIPTION).observe(todo);
		dbc.bindValue(txtDescriptionTarget, observeDescription);

		IObservableValue<Boolean> booleanTarget = WidgetProperties.selection().observe(btnDone);
		IObservableValue<Boolean> observeDone = BeanProperties.value(Todo.FIELD_DONE).observe(todo);
		dbc.bindValue(booleanTarget, observeDone);

		IObservableValue<Date> observeSelectionDateTimeObserveWidget = WidgetProperties.selection().observe(dateTime);
		IObservableValue<Date> observeDueDate = BeanProperties.value(Todo.FIELD_DUEDATE).observe(todo);
		dbc.bindValue(observeSelectionDateTimeObserveWidget, observeDueDate);

		dbc.getBindings().forEach(item -> {
			Binding binding = (Binding) item;
			binding.getTarget().addChangeListener(e -> {
				part.setDirty(true);
			});
		});
	}

	@Focus
	public void onFocus() {
		// the following assumes that you have a Text field
		// called summary
		txtSummary.setFocus();
	}

	@Inject
	@Optional
	private void getNotified(@UIEventTopic(MyEventConstants.TOPIC_TODO_DELETE) Todo todo) {
		if (this.todo.isPresent() && todo.equals(this.todo.get())) {
			part.setToBeRendered(false);
		}
	}

	@PreDestroy
	public void dispose() {
		dbc.dispose();
	}
}
