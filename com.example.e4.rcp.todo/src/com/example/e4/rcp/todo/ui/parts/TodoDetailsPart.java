package com.example.e4.rcp.todo.ui.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.example.e4.rcp.todo.model.ITodoModel;
import com.example.e4.rcp.todo.model.Todo;

public class TodoDetailsPart {
	
	@Inject
	MDirtyable dirty;
	
	private final class MyListener implements ModifyListener {
		@Override
		public void modifyText(ModifyEvent e) {
			if (dirty != null) {
				dirty.setDirty(true);
			}
		}
	}

	private Text summary;
	private Text description;
	private Button btnDone;


	private Todo todo;
	private MyListener listener = new MyListener();
	private DataBindingContext ctx = new DataBindingContext();
	private DateTime dateTime;
	

	@PostConstruct
	public void createControls(Composite parent) {

		GridLayout gl_parent = new GridLayout(2, false);
		gl_parent.marginRight = 10;
		gl_parent.marginLeft = 10;
		gl_parent.horizontalSpacing = 10;
		gl_parent.marginWidth = 0;
		parent.setLayout(gl_parent);

		Label lblSummary = new Label(parent, SWT.NONE);
		lblSummary.setText("Summary");

		summary = new Text(parent, SWT.BORDER);
		summary.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		summary.addModifyListener(listener);

		Label lblDescription = new Label(parent, SWT.NONE);
		lblDescription.setText("Description");

		description = new Text(parent, SWT.BORDER | SWT.MULTI);
		GridData gd_text_1 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1);
		gd_text_1.heightHint = 122;
		description.setLayoutData(gd_text_1);

		description.addModifyListener(listener);

		Label lblNewLabel = new Label(parent, SWT.NONE);
		lblNewLabel.setText("Due Date");

		dateTime = new DateTime(parent, SWT.BORDER);
		dateTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
				1, 1));
		new Label(parent, SWT.NONE);

		btnDone = new Button(parent, SWT.CHECK);
		btnDone.setText("Done");
		ctx = initDataBindings();
	}

	@Persist
	public void save(MDirtyable dirty, ITodoModel model) {
		model.saveTodo(todo);
		dirty.setDirty(false);
	}

	@Inject
	public void setTodo(
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Todo todo) {
		if (todo != null) {
			// Remember the todo as field
			this.todo = todo;
			// update the user interface
			updateUserInterface(todo);
		}
	}

	private void updateUserInterface(Todo todo) {
		ctx.dispose();

		// Check if Ui is available
		// Assumes that one of your fields
		// is called summary
		if (summary != null && !summary.isDisposed()) {
			
			IObservableValue target = WidgetProperties.text(SWT.Modify)
					.observe(summary);
			IObservableValue model = PojoProperties.value("summary").observe(
					todo);
			ctx.bindValue(target, model);
			
			target = WidgetProperties.text(SWT.Modify)
					.observe(description);
			model = PojoProperties.value("description").observe(
					todo);
			ctx.bindValue(target, model);
			target = WidgetProperties.selection()
					.observe(btnDone);
			model = PojoProperties.value("done").observe(
					todo);
			ctx.bindValue(target, model);
			
			IObservableValue observeSelectionDateTimeObserveWidget = WidgetProperties
					.selection().observe(dateTime);
			IObservableValue dueDateTodoObserveValue = PojoProperties.value(
					"dueDate").observe(todo);
			ctx.bindValue(observeSelectionDateTimeObserveWidget,
					dueDateTodoObserveValue, null, null);
		}
		if (dirty != null) {
			dirty.setDirty(false);
		}
	}

	private DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		return bindingContext;
	}
}
