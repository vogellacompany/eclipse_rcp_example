package com.vogella.tasks.ui.parts;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.PersistState;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.widgets.ButtonFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;

import com.vogella.tasks.events.TaskEventConstants;
import com.vogella.tasks.model.Task;
import com.vogella.tasks.model.TaskService;
import com.vogella.tasks.ui.addon.ThemeIconAddon;
import com.vogella.tasks.ui.util.TableColumnPersistenceUtil;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;

public class TodoOverviewPart {

	private static final String CSS_CLASS_KEY = "org.eclipse.e4.ui.css.CssClassName";
	private static final String CREATE_TASK_COMMAND = "com.vogella.tasks.ui.command.createtask";
	private static final String PLUS_ICON = "platform:/plugin/com.vogella.tasks.ui/img/addTask.svg";
	private static final int BUTTON_SIZE = 40;
	private static final int BUTTON_MARGIN_RIGHT = 12;
	private static final int BUTTON_MARGIN_BOTTOM = 24;
	private static final int CIRCLE_INSET = 1;

	@Inject
	MWindow window;

	@Inject
	ESelectionService selectionService;

	@Inject
	TaskService taskService;

	private WritableList<Task> writableList;

	private TableViewer viewer;

	private final TaskStateFilter taskStateFilter = new TaskStateFilter();

	@PostConstruct
	public void createControls(Composite parent, EMenuService menuService, ECommandService commandService,
			EHandlerService handlerService, IThemeEngine themeEngine) {
		//GridLayoutFactory.fillDefaults().numColumns(1).applyTo(parent);

		//ButtonFactory.newButton(SWT.PUSH).text("Load Data").onSelect(e -> update()).create(parent);

		viewer = new TableViewer(parent, SWT.MULTI | SWT.FULL_SELECTION);
		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		parent.setLayout(new FormLayout());
		FormData tableData = new FormData();
		tableData.top = new FormAttachment(0);
		tableData.left = new FormAttachment(0);
		tableData.right = new FormAttachment(100);
		tableData.bottom = new FormAttachment(100);
		table.setLayoutData(tableData);
		createFloatingButton(parent, table, commandService, handlerService, themeEngine);

		// create column for the summary property
		TableViewerColumn colSummary = new TableViewerColumn(viewer, SWT.NONE);
		colSummary.getColumn().setWidth(100);
		colSummary.getColumn().setText("Summary");
 
		
		
		// create column for description property
		TableViewerColumn colDescription = new TableViewerColumn(viewer, SWT.NONE);
		colDescription.getColumn().setWidth(200);
		colDescription.getColumn().setText("Description");

		// Restore column widths if available
		TableColumnPersistenceUtil.restoreColumnWidths(viewer, "TodoOverviewPartTable");

		// use data binding to bind the viewer
		writableList = new WritableList<>();
		// fill the writable list, when Consumer callback is called. Databinding
		// will do the rest once the list is filled
		taskService.consume(writableList::addAll);
		ViewerSupport.bind(viewer, writableList, BeanProperties.values(Task.FIELD_SUMMARY, Task.FIELD_DESCRIPTION));
		viewer.addFilter(taskStateFilter);
		viewer.addSelectionChangedListener(_ -> {
			IStructuredSelection selection = viewer.getStructuredSelection();

			@SuppressWarnings("unchecked")
			List<Task> selectedElements = selection.toList();
			// Make a copy and send out only the copy
			List<Task> copiedElements = selectedElements.stream().map(Task::copy).toList(); // <1>
			selectionService.setSelection(copiedElements);
		});
		// register context menu on the table
		menuService.registerContextMenu(viewer.getControl(), "com.vogella.tasks.ui.popupmenu.table");

	}

	@SuppressWarnings("restriction")
	private void createFloatingButton(Composite parent, Table table, ECommandService commandService,
			EHandlerService handlerService, IThemeEngine themeEngine) {
		Canvas button = new Canvas(parent, SWT.DOUBLE_BUFFERED);
		button.setData(CSS_CLASS_KEY, "floatingButton");
		button.setToolTipText("Create task");
		button.setCursor(button.getDisplay().getSystemCursor(SWT.CURSOR_HAND));

		FormData data = new FormData(BUTTON_SIZE, BUTTON_SIZE);
		data.right = new FormAttachment(100, -BUTTON_MARGIN_RIGHT);
		data.bottom = new FormAttachment(100, -BUTTON_MARGIN_BOTTOM);
		button.setLayoutData(data);
		button.moveAbove(table);

		// a region is the only way to get a non rectangular control over a sibling
		Region circle = new Region();
		circle.add(circle(BUTTON_SIZE));
		button.setRegion(circle);
		button.addDisposeListener(_ -> circle.dispose());

		Image plus = new LocalResourceManager(JFaceResources.getResources(), button)
				.create(ImageDescriptor.createFromURL(toUrl(ThemeIconAddon.themedUri(PLUS_ICON, themeEngine.getActiveTheme().getId()))));
		button.addPaintListener(e -> {
			// a region has hard edges, so blend a smaller circle into the table color inside of it
			e.gc.setBackground(table.getBackground());
			e.gc.fillRectangle(0, 0, BUTTON_SIZE, BUTTON_SIZE);
			e.gc.setAntialias(SWT.ON);
			e.gc.setBackground(button.getBackground());
			e.gc.fillOval(CIRCLE_INSET, CIRCLE_INSET, BUTTON_SIZE - 2 * CIRCLE_INSET, BUTTON_SIZE - 2 * CIRCLE_INSET);
			Rectangle icon = plus.getBounds();
			e.gc.drawImage(plus, (BUTTON_SIZE - icon.width) / 2, (BUTTON_SIZE - icon.height) / 2);
		});
		button.addListener(SWT.MouseUp,
				_ -> handlerService.executeHandler(commandService.createCommand(CREATE_TASK_COMMAND)));
	}

	private static int[] circle(int diameter) {
		int steps = 64;
		double radius = diameter / 2.0;
		int[] points = new int[steps * 2];
		for (int i = 0; i < steps; i++) {
			double angle = 2 * Math.PI * i / steps;
			points[i * 2] = (int) Math.round(radius + radius * Math.cos(angle));
			points[i * 2 + 1] = (int) Math.round(radius + radius * Math.sin(angle));
		}
		return points;
	}

	private static URL toUrl(String uri) {
		try {
			return URI.create(uri).toURL();
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(uri, e);
		}
	}

		private void update() {
			Job job = new Job("loading") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					List<Task> all = taskService.getAll();
					Display.getDefault().asyncExec(() -> updateViewer(all));
					return Status.OK_STATUS;
				}
			};
			job.schedule();
		}

	public void updateViewer(List<Task> list) {
		if (viewer != null) {
			writableList.clear();
			writableList.addAll(list);
		}
	}

	@Inject
	public void setTaskState(@Optional @Named(TaskStateFilter.CONTEXT_KEY) String state) {
		taskStateFilter.setState(state);
		// called before @PostConstruct, when the viewer does not exist yet, and possibly after disposal
		if (viewer != null && !viewer.getTable().isDisposed()) {
			viewer.refresh();
		}
	}

	@Focus
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Inject
	@Optional
	private void subscribeTopicTaskAllTopics(
			@UIEventTopic(TaskEventConstants.TOPIC_TASKS_ALLTOPICS) Map<String, String> event) {
		if (viewer != null) {
			writableList.clear();
			updateViewer(taskService.getAll());
		}
	}

	@PersistState
	public void saveColumnWidths() {
		if (viewer != null && !viewer.getTable().isDisposed()) {
			TableColumnPersistenceUtil.saveColumnWidths(viewer, "TodoOverviewPartTable");
		}
	}

}