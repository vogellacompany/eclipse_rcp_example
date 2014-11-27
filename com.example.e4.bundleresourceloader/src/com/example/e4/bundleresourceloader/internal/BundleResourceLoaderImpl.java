package com.example.e4.bundleresourceloader.internal;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.example.e4.bundleresourceloader.IBundleResourceLoader;

public class BundleResourceLoaderImpl implements IBundleResourceLoader {

	@Override
	public ImageDescriptor getImageDescriptor(Class<?> clazz, String path) {
		Bundle bundle = FrameworkUtil.getBundle(clazz);
		URL url = FileLocator.find(bundle, 
		  new Path(path), null);
		return ImageDescriptor.createFromURL(url);
	}

}
