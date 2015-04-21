package com.example.e4.rcp.todo.parts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import com.example.e4.rcp.todo.events.MyEventConstants;
import com.example.e4.rcp.todo.i18n.Messages;
import com.example.e4.rcp.todo.model.ITodoService;
import com.example.e4.rcp.todo.model.Todo;

public class TodoOverviewPart {

	private Button btnNewButton;
	private Label lblNewLabel;
	private TableViewer viewer;

	@Inject
	UISynchronize sync;

	@Inject
	ESelectionService service;

	@Inject
	EModelService modelService;

	@Inject
	MApplication application;

	@Inject
	IEventBroker broker;

	@Inject
	ITodoService todoService;
	
	private WritableList writableList;
	protected String searchString = "";

	private TableViewerColumn colDescription;
	private TableViewerColumn colSummary;

	@PostConstruct
	public void createControls(Composite parent, EMenuService menuService, @Translation Messages message) {
		parent.setLayout(new GridLayout(1, false));

		btnNewButton = new Button(parent, SWT.PUSH);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				broker.post(MyEventConstants.TOPIC_TODOS_CHANGED, new HashMap<String, String>());
			}
		});

		Text search = new Text(parent, SWT.SEARCH | SWT.CANCEL | SWT.ICON_SEARCH);

		// Assuming that GridLayout is used
		search.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		search.setMessage("Filter");

		// Filter at every keystroke
		search.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				Text source = (Text) e.getSource();
				searchString = source.getText();
				// Trigger update in the viewer
				viewer.refresh();
			}
		});

		// SWT.SEARCH | SWT.CANCEL not supported under Windows7
		// This does not work under Windows7
		search.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				if (e.detail == SWT.CANCEL) {
					Text text = (Text) e.getSource();
					text.setText("");
					//
				}
			}
		});

		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		Table table = viewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		colSummary = new TableViewerColumn(viewer, SWT.NONE);

		colSummary.getColumn().setWidth(100);

		colSummary.setEditingSupport(new EditingSupport(viewer) {

			@Override
			protected void setValue(Object element, Object value) {
				Todo todo = (Todo) element;
				todo.setSummary(String.valueOf(value));
				viewer.refresh();
			}

			@Override
			protected Object getValue(Object element) {
				Todo todo = (Todo) element;
				return todo.getSummary();
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(viewer.getTable(), SWT.NONE);
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
		colDescription = new TableViewerColumn(viewer, SWT.NONE);

		colDescription.getColumn().setWidth(100);

		// We search in the summary and description field
		viewer.addFilter(new ViewerFilter() {
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				Todo todo = (Todo) element;
				return todo.getSummary().contains(searchString) || todo.getDescription().contains(searchString);
			}
		});

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
				service.setSelection(selection.getFirstElement());
			}
		});
		menuService.registerContextMenu(viewer.getControl(), "com.example.e4.rcp.todo.popupmenu.table");
		writableList = new WritableList(todoService.getTodos(), Todo.class);
		ViewerSupport.bind(viewer, writableList,
				BeanProperties.values(new String[] { Todo.FIELD_SUMMARY, Todo.FIELD_DESCRIPTION }));

		translateTable(message);

	}

	@Inject
	@Optional
	private void subscribeTopicTodoAllTopics(
			@UIEventTopic(MyEventConstants.TOPIC_TODO_ALLTOPICS) Map<String, String> event) {
		Job job = new Job("loading") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				final List<Todo> list = todoService.getTodos();
				sync.asyncExec(new Runnable() {
					@Override
					public void run() {
						if (viewer != null) {
							writableList.clear();
							writableList.addAll(list);
						}
					}
				});
				return Status.OK_STATUS;
			}
		};
		job.schedule();
		
		
	}

	@Focus
	private void setFocus() {
		btnNewButton.setFocus();
	}

	@Inject
	public void translateTable(@Translation Messages message) {
		if (viewer != null && !viewer.getTable().isDisposed()) {
			colSummary.getColumn().setText(message.txtSummary);
			colDescription.getColumn().setText(message.txtDescription);
			btnNewButton.setText(message.buttonLoadData);
		}
	}
}
