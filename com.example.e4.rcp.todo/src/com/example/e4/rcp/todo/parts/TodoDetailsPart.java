package com.example.e4.rcp.todo.parts;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.sideeffect.CompositeSideEffect;
import org.eclipse.core.databinding.observable.sideeffect.ISideEffectFactory;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.swt.WidgetSideEffects;
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

	private boolean dirty;
	
	
	
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

			ISideEffectFactory sideEffectFactory = WidgetSideEffects.createFactory(txtSummary);

			IObservableValue<String> txtSummaryTarget = WidgetProperties.text(SWT.Modify).observe(txtSummary);
			IObservableValue<String> observeSummary = PojoProperties.value(Todo.FIELD_SUMMARY).observeDetail(observableTodo);
			
			sideEffectFactory.create(observeSummary::getValue, txtSummaryTarget::setValue);
			sideEffectFactory.create(txtSummaryTarget::getValue, summary -> {
				todo.ifPresent(t -> {
					t.setSummary(summary);
					dirtyable.setDirty(dirty);
				});
			});

			IObservableValue<String> txtDescriptionTarget = WidgetProperties.text(SWT.Modify).observe(txtDescription);
			IObservableValue<String> observeDescription = PojoProperties.value(Todo.FIELD_DESCRIPTION).observeDetail(observableTodo);
			sideEffectFactory.create(observeDescription::getValue, txtDescriptionTarget::setValue);
			sideEffectFactory.create(txtDescriptionTarget::getValue, description -> {
				todo.ifPresent(t -> {
					t.setDescription(description);
					dirtyable.setDirty(dirty);
				});
			});

			IObservableValue<Boolean> booleanTarget = WidgetProperties.selection().observe(btnDone);
			IObservableValue<Boolean> observeDone = PojoProperties.value(Todo.FIELD_DONE).observeDetail(observableTodo);
			sideEffectFactory.create(observeDone::getValue, booleanTarget::setValue);
			sideEffectFactory.create(booleanTarget::getValue, done -> {
				todo.ifPresent(t -> {
					t.setDone(done);
					dirtyable.setDirty(dirty);
				});
			});

			IObservableValue<Date> observeSelectionDateTimeObserveWidget = WidgetProperties.selection()
					.observe(dateTime);
			IObservableValue<Date> observeDueDate = PojoProperties.value(Todo.FIELD_DUEDATE).observeDetail(observableTodo);
			sideEffectFactory.create(observeDueDate::getValue, date -> {
				if(date != null) {
					observeSelectionDateTimeObserveWidget.setValue(date);
				}
			});
			sideEffectFactory.create(observeSelectionDateTimeObserveWidget::getValue, dueDate -> {
				todo.ifPresent(t -> {
					t.setDueDate(dueDate);
					dirtyable.setDirty(dirty);
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
			CompositeSideEffect compositeSideEffect = (CompositeSideEffect) txtSummary
					.getData(CompositeSideEffect.class.getName());
			compositeSideEffect.pause();
			dirty = false;
			this.observableTodo.setValue(todo.get());
			compositeSideEffect.resumeAndRunIfDirty();
			dirty = true;
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
}
