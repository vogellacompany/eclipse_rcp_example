package com.example.e4.rcp.todo.ui.parts;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class PlaygroundPart {
	private Text text;

	@PostConstruct
	public void createControls(Composite parent, final MDirtyable dirty) {
		
		text = new Text(parent, SWT.MULTI | SWT.LEAD | SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		text.setSize(200, 100);
		text.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				dirty.setDirty(true);
			}
		});
		
	}
	@Focus
	public void setFocus() {
		text.setFocus();
	}
	
	@Persist
	public void persists(MDirtyable dirty) {
		dirty.setDirty(false);
		System.out.println("PlaygroundPart data persisted...");
	}
	
	// Declare a field label, required for @Focus
		Label label;

		@PostConstruct
		public void createControls(Composite parent) {
			label = new Label(parent, SWT.NONE);
		}
}
