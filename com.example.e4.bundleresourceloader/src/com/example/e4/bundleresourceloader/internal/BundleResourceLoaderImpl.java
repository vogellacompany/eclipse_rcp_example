package com.example.e4.bundleresourceloader.internal;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.example.e4.bundleresourceloader.IBundleResourceLoader;

public class BundleResourceLoaderImpl implements IBundleResourceLoader {

	@Override
	public Image loadImage(Class<?> clazz, String path) {
		Bundle bundle = FrameworkUtil.getBundle(clazz);
		URL url = FileLocator.find(bundle, 
		  new Path(path), null);
		ImageDescriptor imageDescr = ImageDescriptor.createFromURL(url);
		return imageDescr.createImage(); 
	}

}
