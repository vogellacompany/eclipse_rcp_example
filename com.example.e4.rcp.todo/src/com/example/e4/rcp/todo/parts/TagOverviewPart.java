package com.example.e4.rcp.todo.parts;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.dialogs.filteredtree.FilteredTree;
import org.eclipse.e4.ui.dialogs.filteredtree.PatternFilter;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.databinding.viewers.ObservableListTreeContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.example.e4.rcp.todo.databinding.DelegatingDescriptionProperty;
import com.example.e4.rcp.todo.databinding.DelegatingLabelProperty;
import com.example.e4.rcp.todo.databinding.DelegatingObservableCellLabelProvider;
import com.example.e4.rcp.todo.databinding.TagTreeListProperty;
import com.example.e4.rcp.todo.model.ITodoService;
import com.example.e4.rcp.todo.model.Tag;
import com.example.e4.rcp.todo.model.Todo;

public class TagOverviewPart {

	@Inject
	private ESelectionService selectionService;

	@PostConstruct
	public void postConstruct(Composite parent, ITodoService todoService) {
		FilteredTree filteredTree = new FilteredTree(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER,
				new PatternFilter());
		filteredTree.getViewer().getTree().setHeaderVisible(true);

		ObservableListTreeContentProvider observableContentProvider = new ObservableListTreeContentProvider(
				new TagTreeListProperty().listFactory(), null);
		filteredTree.getViewer().setContentProvider(observableContentProvider);
		filteredTree.getViewer().addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				selectionService.setSelection(filteredTree.getViewer().getStructuredSelection().getFirstElement());
			}
		});

		TreeViewerColumn labelViewerColumn = new TreeViewerColumn(filteredTree.getViewer(), SWT.NONE);
		labelViewerColumn.getColumn().setWidth(200);
		labelViewerColumn.getColumn().setText("Label");

		DelegatingLabelProperty labelProperty = new DelegatingLabelProperty();
		labelViewerColumn.setLabelProvider(
				new DelegatingObservableCellLabelProvider(observableContentProvider.getKnownElements(), labelProperty));

		TreeViewerColumn descriptionViewerColumn = new TreeViewerColumn(filteredTree.getViewer(), SWT.NONE);
		descriptionViewerColumn.getColumn().setWidth(200);
		descriptionViewerColumn.getColumn().setText("Description");

		DelegatingDescriptionProperty descriptionProperty = new DelegatingDescriptionProperty();
		descriptionViewerColumn.setLabelProvider(new DelegatingObservableCellLabelProvider(
				observableContentProvider.getKnownElements(), descriptionProperty));

		filteredTree.getViewer().setInput(createInput(todoService));
	}

	private Tag<Tag<Todo>> createInput(ITodoService todoService) {
		List<Todo> todos = todoService.getTodos();
		Tag<Todo> eclipseTag = new Tag<>("Eclipse", todos);
		List<Tag<Todo>> arrayList = new ArrayList<>();
		arrayList.add(eclipseTag);
		Tag<Tag<Todo>> rootTag = new Tag<>("root", arrayList);
		return rootTag;
	}

	private class TagTreeContentProvider implements ITreeContentProvider {

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof Tag) {
				return ((Tag<?>) parentElement).getTaggedElements().toArray();
			}

			return null;
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			return (element instanceof Tag) && (!(((Tag<?>) element).getTaggedElements().isEmpty()));
		}
	}

}