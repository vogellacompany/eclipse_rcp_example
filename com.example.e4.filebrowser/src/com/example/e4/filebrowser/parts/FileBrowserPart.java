package com.example.e4.filebrowser.parts;

import java.io.File;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class FileBrowserPart {
	private TreeViewer viewer;

	@PostConstruct
	public void createControls(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(File.listRoots());
		Tree tree = (Tree) viewer.getControl();
		tree.addSelectionListener(new SelectionAdapter() {
		  @Override
		  public void widgetSelected(SelectionEvent e) {
			  TreeItem item = (TreeItem) e.item;
			  	if (item.getItemCount() > 0) {
			  		item.setExpanded(!item.getExpanded());
			  		// update the viewer
			  		viewer.refresh();
			  	}
		  	}
		});
	}

	class ViewContentProvider implements ITreeContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return (File[]) inputElement;
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			File file = (File) parentElement;
			return file.listFiles();
		}

		@Override
		public Object getParent(Object element) {
			File file = (File) element;
			return file.getParentFile();
		}

		@Override
		public boolean hasChildren(Object element) {
			File file = (File) element;
			if (file.isDirectory()) {
				return true;
			}
			return false;
		}

	}

	class ViewLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			File file = (File) element;
			String name = file.getName();
			return name.length() > 0 ? name : file.getPath();
		}

		public Image getImage(Object obj) {
			Bundle bundle = FrameworkUtil.getBundle(this.getClass());
			URL url = FileLocator
					.find(bundle, new Path("icons/test.gif"), null);
			ImageDescriptor image = ImageDescriptor.createFromURL(url);
			return image.createImage();
		}
	}

	@Focus
	public boolean setFocus() {
		viewer.getControl().setFocus();
		return true;
	}
}
