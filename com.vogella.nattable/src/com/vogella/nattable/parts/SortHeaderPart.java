package com.vogella.nattable.parts;

import static org.eclipse.jface.widgets.WidgetFactory.button;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.ConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.extension.e4.selection.E4SelectionListener;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.GlazedListsEventLayer;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.groupBy.GroupByDataLayer;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.groupBy.GroupByHeaderLayer;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.groupBy.GroupByHeaderMenuConfiguration;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.groupBy.GroupByModel;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultRowHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.CornerLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultColumnHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultRowHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.RowHeaderLayer;
import org.eclipse.nebula.widgets.nattable.layer.AbstractLayerTransform;
import org.eclipse.nebula.widgets.nattable.layer.CompositeLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayerListener;
import org.eclipse.nebula.widgets.nattable.layer.event.ILayerEvent;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.sort.config.SingleClickSortConfiguration;
import org.eclipse.nebula.widgets.nattable.tree.TreeLayer;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.vogella.tasks.model.Task;
import com.vogella.tasks.model.TaskService;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.TransformedList;

public class SortHeaderPart {

	@Inject ESelectionService selectionService;
	
	private static final String[] propertyNames = { "summary", "description", "dueDate", "done" };

	private static final Map<String, String> propertyToLabelMap;

	private BodyLayerStack<Task> bodyLayerStack;

	static {
		propertyToLabelMap = new HashMap<>();
		propertyToLabelMap.put("summary", "Summary");
		propertyToLabelMap.put("description", "Description");
		propertyToLabelMap.put("dueDate", "Due Date");
		propertyToLabelMap.put("done", "Done");
	}

	@PostConstruct
	public void postConstruct(Composite parent, TaskService taskService) {

		parent.setLayout(new GridLayout(1, false));
		button(SWT.PUSH).onSelect(e-> bodyLayerStack.getList().remove(0)).text("Delete").create(parent);
		button(SWT.PUSH).onSelect(e-> bodyLayerStack.getList().remove(0)).text("Delete").create(parent);
		ConfigRegistry configRegistry = new ConfigRegistry();

//		// Data setup
//		EventList<Task> tasks = GlazedLists.eventList(taskService.getAll());
//		SortedList<Task> sortedList = new SortedList<>(tasks, null);

		IColumnPropertyAccessor<Task> accessor = new TaskColumnPropertyAccessor();
//		IDataProvider bodyDataProvider = new ListDataProvider<>(sortedList, accessor);

		bodyLayerStack = new BodyLayerStack<>(taskService.getAll(), accessor);

		// build the column header layer
		IDataProvider columnHeaderDataProvider = new DefaultColumnHeaderDataProvider(propertyNames, propertyToLabelMap);
		DataLayer columnHeaderDataLayer = new DefaultColumnHeaderDataLayer(columnHeaderDataProvider);
		ColumnHeaderLayer columnHeaderLayer = new ColumnHeaderLayer(columnHeaderDataLayer, bodyLayerStack,
				bodyLayerStack.getSelectionLayer());

		// build the row header layer
		IDataProvider rowHeaderDataProvider = new DefaultRowHeaderDataProvider(bodyLayerStack.getBodyDataProvider());
		DataLayer rowHeaderDataLayer = new DefaultRowHeaderDataLayer(rowHeaderDataProvider);
		ILayer rowHeaderLayer = new RowHeaderLayer(rowHeaderDataLayer, bodyLayerStack,
				bodyLayerStack.getSelectionLayer());

		// build the corner layer
		IDataProvider cornerDataProvider = new DefaultCornerDataProvider(columnHeaderDataProvider,
				rowHeaderDataProvider);
		DataLayer cornerDataLayer = new DataLayer(cornerDataProvider);
		ILayer cornerLayer = new CornerLayer(cornerDataLayer, rowHeaderLayer, columnHeaderLayer);

		// build the grid layer
		GridLayer gridLayer = new GridLayer(bodyLayerStack, columnHeaderLayer, rowHeaderLayer, cornerLayer);

		// set the group by header on top of the grid
		CompositeLayer compositeGridLayer = new CompositeLayer(1, 2);
		final GroupByHeaderLayer groupByHeaderLayer = new GroupByHeaderLayer(bodyLayerStack.getGroupByModel(),
				gridLayer, columnHeaderDataProvider, columnHeaderLayer);
		compositeGridLayer.setChildLayer(GroupByHeaderLayer.GROUP_BY_REGION, groupByHeaderLayer, 0, 0);
		compositeGridLayer.setChildLayer("Grid", gridLayer, 0, 1);

		// turn the auto configuration off as we want to add our header menu
		// configuration
		NatTable natTable = new NatTable(parent, compositeGridLayer, false);

		// as the autoconfiguration of the NatTable is turned off, we have to
		// add the DefaultNatTableStyleConfiguration and the ConfigRegistry
		// manually
		natTable.setConfigRegistry(configRegistry);
		natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
		natTable.addConfiguration(new SingleClickSortConfiguration());

		// add group by configuration
		natTable.addConfiguration(new GroupByHeaderMenuConfiguration(natTable, groupByHeaderLayer));

//		
//		DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);
//
//		SelectionLayer selectionLayer = new SelectionLayer(bodyDataLayer);
//		ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);
//
//		IDataProvider columnHeaderDataProvider = new DefaultColumnHeaderDataProvider(
//				propertyNames,propertyToLabelMap);
//		DataLayer columnHeaderDataLayer = new DefaultColumnHeaderDataLayer(columnHeaderDataProvider);
//		ILayer columnHeaderLayer = new ColumnHeaderLayer(columnHeaderDataLayer, viewportLayer, selectionLayer);
//
//		SortHeaderLayer<Person> sortHeaderLayer = new SortHeaderLayer<>(columnHeaderLayer,
//				new GlazedListsSortModel<>(sortedList, accessor, configRegistry, columnHeaderDataLayer));
//
//		CompositeLayer compositeLayer = new CompositeLayer(1, 2);
//		compositeLayer.setChildLayer(GridRegion.COLUMN_HEADER, sortHeaderLayer, 0, 0);
//		compositeLayer.setChildLayer(GridRegion.BODY, viewportLayer, 0, 1);

//		NatTable natTable = new NatTable(parent, compositeLayer, false);
//
//		natTable.setConfigRegistry(configRegistry);
//		natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
//		natTable.addConfiguration(new SingleClickSortConfiguration());

		natTable.configure();
	}

	/**
	 * Always encapsulate the body layer stack in an AbstractLayerTransform to
	 * ensure that the index transformations are performed in later commands.
	 *
	 * @param <T>
	 */
	class BodyLayerStack<T> extends AbstractLayerTransform {

		private final SortedList<T> sortedList;

		private final IDataProvider bodyDataProvider;

		private final SelectionLayer selectionLayer;

		private final GroupByModel groupByModel = new GroupByModel();

		private EventList<T> eventList;

		public BodyLayerStack(List<T> values, IColumnPropertyAccessor<T> columnPropertyAccessor) {
			eventList = GlazedLists.eventList(values);
			TransformedList<T, T> rowObjectsGlazedList = GlazedLists.threadSafeList(eventList);

			// use the SortedList constructor with 'null' for the Comparator
			// because the Comparator
			// will be set by configuration
			this.sortedList = new SortedList<>(rowObjectsGlazedList, null);

			// Use the GroupByDataLayer instead of the default DataLayer
			GroupByDataLayer<T> bodyDataLayer = new GroupByDataLayer<>(getGroupByModel(), this.sortedList,
					columnPropertyAccessor);
			// get the IDataProvider that was created by the GroupByDataLayer
			this.bodyDataProvider = bodyDataLayer.getDataProvider();
			// layer for event handling of GlazedLists and PropertyChanges
			GlazedListsEventLayer<T> glazedListsEventLayer = new GlazedListsEventLayer<>(bodyDataLayer,
					this.sortedList);

			this.selectionLayer = new SelectionLayer(glazedListsEventLayer);
			bodyDataLayer.getSortModel();
			
			
			// add a tree layer to visualise the grouping
			TreeLayer treeLayer = new TreeLayer(this.selectionLayer, bodyDataLayer.getTreeRowModel());

			
			
			ViewportLayer viewportLayer = new ViewportLayer(treeLayer);

			setUnderlyingLayer(viewportLayer);
		}

		public SelectionLayer getSelectionLayer() {
			return this.selectionLayer;
		}

		public SortedList<T> getSortedList() {
			return this.sortedList;
		}

		public IDataProvider getBodyDataProvider() {
			return this.bodyDataProvider;
		}

		public GroupByModel getGroupByModel() {
			return this.groupByModel;
		}
		public EventList<T> getList(){
			return eventList;
		}
	}
}
