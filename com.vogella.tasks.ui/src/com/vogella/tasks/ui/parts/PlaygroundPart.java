package com.vogella.tasks.ui.parts;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.vogella.imageloader.services.IBundleResourceLoader;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

public class PlaygroundPart {
	private Text text;
	private Browser browser;
	private Text target;


	@Inject
	MPart part;
	@Inject
	IBundleResourceLoader loader;

	@PostConstruct
	public void createControls(Composite parent) {

		Label label = new Label(parent, SWT.NONE);
        
        // the following code assumes that you have a vogella.png file
        // in a folder called "images" in this plug-in
        ResourceManager resourceManager = 
                new LocalResourceManager(JFaceResources.getResources(), label);
        Image image = resourceManager.
                create(loader.getImageDescriptor(this.getClass(), "images/vogella.png"));
        label.setImage(image);
		
	}

	@Persist
	public void saveItReallyReally() {
		// TODO really do the saving
		part.setDirty(false);
	}

	@Inject
	public void getFromOSGi(@Optional @EventTopic("YOURKEY") String value) {
		System.out.println(value);
	}
}