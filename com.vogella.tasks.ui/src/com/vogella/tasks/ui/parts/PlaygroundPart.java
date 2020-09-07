package com.vogella.tasks.ui.parts;

import javax.annotation.PostConstruct;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.vogella.imageloader.services.IBundleResourceLoader;

public class PlaygroundPart {
	@PostConstruct
	public void createControls(Composite parent, IBundleResourceLoader loader) {
		Label label = new Label(parent, SWT.NONE);

		// the following code assumes that you have a vogella.png file
		// in a folder called "images" in this plug-in
		ResourceManager resourceManager = new LocalResourceManager(JFaceResources.getResources(), label);
		Image image = resourceManager.createImage(loader.getImageDescriptor(this.getClass(), "images/vogella.png"));
		label.setImage(image);
    }
}