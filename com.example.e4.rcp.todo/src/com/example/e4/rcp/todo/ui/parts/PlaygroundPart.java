package com.example.e4.rcp.todo.ui.parts;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.example.e4.bundleresourceloader.IBundleResourceLoader;

public class PlaygroundPart {
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

}
