package com.vogella.tasks.ui.parts;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.PersistState;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.widgets.ButtonFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;

import com.vogella.swt.widgets.SegmentedBar;
import com.vogella.swt.widgets.SegmentedBar.Segment;
import com.vogella.tasks.events.TaskEventConstants;
import com.vogella.tasks.model.Task;
import com.vogella.tasks.model.TaskService;
import com.vogella.tasks.ui.util.TableColumnPersistenceUtil;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

public class TodoOverviewPart {

	@Inject
	MWindow window;

	@Inject
	ESelectionService selectionService;

	@Inject
	TaskService taskService;

	private WritableList<Task> writableList;

	private TableViewer viewer;

	private SegmentedBar summaryBar;

	private TaskCategory categoryFilter;

	@PostConstruct
	public void createControls(Composite parent, EMenuService menuService) {
		//GridLayoutFactory.fillDefaults().numColumns(1).applyTo(parent);

		//ButtonFactory.newButton(SWT.PUSH).text("Load Data").onSelect(e -> update()).create(parent);

		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		parent.setLayout(layout);

		summaryBar = new SegmentedBar(parent, SWT.NONE);
		summaryBar.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		summaryBar.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> {
			updateCategoryFilter();
			viewer.refresh();
		}));

		viewer = new TableViewer(parent, SWT.MULTI | SWT.FULL_SELECTION);
		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

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
		viewer.addFilter(new ViewerFilter() {
			@Override
			public boolean select(Viewer v, Object parentElement, Object element) {
				return categoryFilter == null
						|| TaskCategory.of((Task) element, LocalDate.now()) == categoryFilter;
			}
		});
		updateSummary(taskService.getAll());
		ViewerSupport.bind(viewer, writableList, BeanProperties.values(Task.FIELD_SUMMARY, Task.FIELD_DESCRIPTION));
		viewer.addSelectionChangedListener(event -> {
			IStructuredSelection selection = viewer.getStructuredSelection();

			@SuppressWarnings("unchecked")
			List<Task> selectedElements = selection.toList();
			// Make a copy and send out only the copy
			List<Task> copiedElements = selectedElements.stream().map(t -> t.copy()).collect(Collectors.toList()); // <1>
			selectionService.setSelection(copiedElements);
		});
		// register context menu on the table
		menuService.registerContextMenu(viewer.getControl(), "com.vogella.tasks.ui.popupmenu.table");

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
			updateSummary(list);
		}
	}

	private void updateSummary(List<Task> tasks) {
		LocalDate today = LocalDate.now();
		List<Segment> segments = Arrays.stream(TaskCategory.values()).map(category -> {
			long count = tasks.stream().filter(task -> TaskCategory.of(task, today) == category).count();
			return new Segment(category.name(), category.label(), (int) count, category.color());
		}).toList();
		summaryBar.setSegments(segments);
		// the bar clears its selection if the selected category became empty
		updateCategoryFilter();
		viewer.refresh();
	}

	private void updateCategoryFilter() {
		String id = summaryBar.getSelectedId();
		categoryFilter = id == null ? null : TaskCategory.valueOf(id);
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