package com.vogella.imageloader.services;

import org.eclipse.jface.resource.ImageDescriptor;

public interface IBundleResourceLoader {

    // service does NOT recycle the provided image
    // the consumer of the image is responsible for calling the dispose()
    // method on the created Image object once the Image is not required anymore
    // can be automated with the usage of LocalResourceManager

    public ImageDescriptor getImageDescriptor(Class<?> clazz, String path);
}