package com.example.e4.rcp.todo.ui.parts;

import java.net.URL;

import javax.annotation.PostConstruct;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.internal.adaptor.IModel;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.example.e4.rcp.todo.model.ITodoModel;
import com.example.e4.rcp.todo.model.Todo;
import org.eclipse.swt.layout.GridData;

public class TodoOverviewPart {

	private Button btnNewButton;
	private Label lblNewLabel;

	@PostConstruct
	public void createControls(Composite parent, ITodoModel model) {
		System.out.println(model.getTodos().size());
		parent.setLayout(new GridLayout(1, false));

		btnNewButton = new Button(parent, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				lblNewLabel.setText("Number of Todo items: 7");
			}
		});

		btnNewButton.setText("Load Data");

		TableViewer viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION);
		Table table = viewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer.setContentProvider(ArrayContentProvider.getInstance());

		TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn tableColumn = column.getColumn();
		tableColumn.setWidth(200);
		tableColumn.setText("Summary");
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				Todo todo = (Todo) element;

				return todo.getSummary();
			};
		});
		
		column = new TableViewerColumn(viewer, SWT.NONE);
		tableColumn = column.getColumn();
		tableColumn.setWidth(200);
		tableColumn.setText("Description");
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				Todo todo = (Todo) element;

				return todo.getDescription();
			};
		}

		);
		
		viewer.setInput(model.getTodos());

	}

	@Focus
	private void setFocus() {
		btnNewButton.setFocus();
	}

	

}
