package com.example.e4.bundleresourceloader;

import org.eclipse.jface.resource.ImageDescriptor;

public interface IBundleResourceLoader {
	public ImageDescriptor loadImage(Class<?> clazz, String path);
}
