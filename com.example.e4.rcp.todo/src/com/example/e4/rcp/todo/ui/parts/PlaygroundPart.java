package com.example.e4.rcp.todo.ui.parts;

import javax.annotation.PostConstruct;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.core.databinding.beans.PojoProperties;

public class PlaygroundPart {
	private DataBindingContext m_bindingContext;
	private Text eins;
	private Text zwei;

	@PostConstruct
	public void createControls(Composite parent, final MDirtyable dirty) {
		dirty.setDirty(true);
		parent.setLayout(new GridLayout(1, false));
		eins = new Text(parent, SWT.BORDER);
		eins.setText("Hello1");
		eins.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		zwei = new Text(parent, SWT.BORDER);
		zwei.setText("Hello2");
		zwei.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnClick = new Button(parent, SWT.CHECK);
		btnClick.setText("Click");
		m_bindingContext = initDataBindings();

	}
	
	@Focus
	public void setFocus() {
		eins.setFocus();
	}
	
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextEinsObserveWidget = WidgetProperties.text(SWT.Modify).observe(eins);
		IObservableValue textZweiObserveValue = PojoProperties.value("text").observe(zwei);
		bindingContext.bindValue(observeTextEinsObserveWidget, textZweiObserveValue, null, null);
		//
		return bindingContext;
	}
}
