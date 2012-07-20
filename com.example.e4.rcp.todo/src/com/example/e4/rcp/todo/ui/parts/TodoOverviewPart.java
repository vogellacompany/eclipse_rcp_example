package com.example.e4.rcp.todo.ui.parts;

import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.property.Properties;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.example.e4.rcp.todo.model.ITodoModel;
import com.example.e4.rcp.todo.model.Todo;

public class TodoOverviewPart {
	private DataBindingContext m_bindingContext;

	private Button btnNewButton;
	private Label lblNewLabel;
	private TableViewer viewer;

	private List<Todo> list;

	@PostConstruct
	public void createControls(Composite parent, ITodoModel model) {
		 list = model.getTodos();
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

		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION);
		Table table = viewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setWidth(200);
		column = new TableColumn(table, SWT.NONE);
		column.setWidth(200);
		column = new TableColumn(table, SWT.NONE);
		column.setWidth(200);
		column = new TableColumn(table, SWT.NONE);
		column.setWidth(200);
		m_bindingContext = initDataBindings();
	}

	@Focus
	private void setFocus() {
		btnNewButton.setFocus();
	}

	


	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableList selfList = Properties.selfList(Todo.class).observe(list);
		ViewerSupport.bind(viewer, selfList, PojoProperties.values(Todo.class, new String[]{"summary", "description", "done", "dueDate"}));
		//
		return bindingContext;
	}
}
