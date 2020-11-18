package com.vogella.nattable.parts;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.selection.config.DefaultRowSelectionLayerConfiguration;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.vogella.tasks.model.Task;
import com.vogella.tasks.model.TaskService;

public class NattableExample {

	@PostConstruct
	public void postConstruct(Composite parent, TaskService taskService) {
		parent.setLayout(new GridLayout());

		IColumnPropertyAccessor<Task> columnPropertyAccessor = new TaskColumnPropertyAccessor();
		IDataProvider bodyDataProvider = new ListDataProvider<Task>(taskService.getAll(), columnPropertyAccessor);

		// create layer stack consisting out of:
		// data layer
		// selection layer
		// viewport layer
		DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);
		setColumnWidth(bodyDataLayer);

		// selection layer
		SelectionLayer selectionLayer = new SelectionLayer(bodyDataLayer);
		// Enable full selection for rows
		selectionLayer.addConfiguration(new DefaultRowSelectionLayerConfiguration());

		// view port layer
		ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);

		// as the selection mouse bindings are registered for the region label
		// GridRegion.BODY
		// we need to set that region label to the viewport so the selection via mouse
		// is working correctly
		viewportLayer.setRegionName(GridRegion.BODY);


		NatTable natTable = new NatTable(parent, viewportLayer);

		GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);
	}

	private void setColumnWidth(DataLayer bodyDataLayer) {
		// activate percentage sizing for all columns
		bodyDataLayer.setColumnPercentageSizing(true);

		// Use percentage values for the first three columns column
		bodyDataLayer.setColumnWidthPercentageByPosition(0, 5);
		bodyDataLayer.setColumnWidthPercentageByPosition(1, 30);
		bodyDataLayer.setColumnWidthPercentageByPosition(2, 40);

		// deactivate percentage sizing for the fourth column (isDone)
		bodyDataLayer.setColumnPercentageSizing(3, false);
		// apply a fixed size for the fourth column
		bodyDataLayer.setColumnWidthByPosition(3, 50);

		// also use percentage sizing for the dueDate column
		bodyDataLayer.setColumnWidthPercentageByPosition(4, 25);
	}




}