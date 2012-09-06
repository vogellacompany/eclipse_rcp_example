package com.example.e4.rcp.todo.ui.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.example.e4.bundleresourceloader.IBundleResourceLoader;

public class PlaygroundPart {
	@Inject
	@Preference(nodePath = "com.example.e4.rcp.todo", value = "user")
	String userTodo;
	private Label label;

	@PostConstruct
	public void createControls(Composite parent, IBundleResourceLoader loader) {
		label = new Label(parent, SWT.NONE);
		// This assumes you have a vogella.png file
		// in folder images
		label.setImage(loader.loadImage(this.getClass(), "images/vogella.png"));

	}

	@Focus
	public void setFocus() {
		label.setFocus();
	}

	@Inject
	@Optional
	public void trackUserSettings(
			@Preference(nodePath = "com.example.e4.rcp.todo", value = "user") String user) {
		System.out.println("New user: " + user);
		System.out.println("Field: " + userTodo);
	}

	@Inject
	@Optional
	public void trackPasswordSettings(
			@Preference(nodePath = "com.example.e4.rcp.todo", value = "password") String password) {
		System.out.println("New password: " + password);
	}

}
