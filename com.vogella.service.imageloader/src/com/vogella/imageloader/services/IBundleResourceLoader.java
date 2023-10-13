package com.vogella.imageloader.services;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

public interface IBundleResourceLoader {

    // service does NOT dispose the provided image
    // the consumer of the image is responsible for calling the dispose()
    // method on the created Image object once the Image is not required anymore
    // can be automated with the usage of LocalResourceManager

    public ImageDescriptor getImageDescriptor(Class<?> clazz, String path);
    
    // service does NOT dispose the provided image nor the created
    // caller needs to dispose them
    public Image resize(Image image, int width, int height);
}