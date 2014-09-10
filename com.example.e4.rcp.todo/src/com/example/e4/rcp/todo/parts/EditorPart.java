package com.example.e4.rcp.todo.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
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
	MDirtyable dirty;
	
	@Inject MPart part;

	private Text txtSummary;
	private Text txtDescription;
	private Button btnDone;
	private DateTime dateTime;
	
	private DataBindingContext ctx = new DataBindingContext();

	// define listener for the data binding
	IChangeListener listener = new IChangeListener() {
		@Override
		public void handleChange(ChangeEvent event) {
			if (dirty!=null){
				dirty.setDirty(true);
			}
		}
	};
	private Todo todo;

	@PostConstruct
	public void createControls(Composite parent, ITodoService todoService) {
		// extract the id of the todo item
		String string = part.getPersistedState().get(Todo.FIELD_ID);
		Long idOfTodo = Long.valueOf(string);
		
		// retrieve the todo based on the persistedState
		todo = todoService.getTodo(idOfTodo);
		
		GridLayout gl_parent = new GridLayout(2, false);
		gl_parent.marginRight = 10;
		gl_parent.marginLeft = 10;
		gl_parent.horizontalSpacing = 10;
		gl_parent.marginWidth = 0;
		parent.setLayout(gl_parent);

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
		dateTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
				1, 1));
		new Label(parent, SWT.NONE);

		btnDone = new Button(parent, SWT.CHECK);
		btnDone.setText("Done");
		
		updateUserInterface(todo);
	}

	@Persist
	public void save(ITodoService model) {
		model.saveTodo(todo);
		dirty.setDirty(false);
	}

	private void updateUserInterface(Todo todo) {
		
		// check if the user interface is available
		// assume you have a field called "summary"
		// for a widget
		if (txtSummary != null && !txtSummary.isDisposed()) {
			
			
			// Deregister change listener to the old binding
			IObservableList providers = ctx.getValidationStatusProviders();
			for (Object o : providers) {
				Binding b = (Binding) o;
				b.getTarget().removeChangeListener(listener);
			}
			
			// Remove bindings
			ctx.dispose();
			
			IObservableValue target = WidgetProperties.text(SWT.Modify)
					.observe(txtSummary);
			IObservableValue model = BeanProperties.value(Todo.FIELD_SUMMARY).observe(
					todo);
			ctx.bindValue(target, model);

			target = WidgetProperties.selection().observe(btnDone);
			model = BeanProperties.value(Todo.FIELD_DONE).observe(todo);
			ctx.bindValue(target, model);
			
			target = WidgetProperties.selection().observe(btnDone);
			model = BeanProperties.value(Todo.FIELD_DONE).observe(todo);
			ctx.bindValue(target, model);

			IObservableValue observeSelectionDateTimeObserveWidget = 
					WidgetProperties
					.selection().observe(dateTime);
			IObservableValue dueDateTodoObserveValue = 
					BeanProperties.value(
					Todo.FIELD_DUEDATE).observe(todo);
			ctx.bindValue(observeSelectionDateTimeObserveWidget,
					dueDateTodoObserveValue);
			
			// register listener for any changes
			providers = ctx.getValidationStatusProviders();
			for (Object o : providers) {
				Binding b = (Binding) o;
				b.getTarget().addChangeListener(listener);
			}
			
			// Register for the changes
			providers = ctx.getValidationStatusProviders();
			for (Object o : providers) {
				Binding b = (Binding) o;
				b.getTarget().addChangeListener(listener);
			}
		}
	}

	@Focus
	public void onFocus() {
		// the following assumes that you have a Text field
		// called summary
		txtSummary.setFocus();
	}
	
	@Inject
	@Optional
	private void getNotified(
			@UIEventTopic(MyEventConstants.TOPIC_TODO_DELETE) Todo todo) {
		if (this.todo.equals(todo)){
			part.setToBeRendered(false);
		}
	}
}
