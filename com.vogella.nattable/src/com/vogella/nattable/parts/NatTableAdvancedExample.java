
package com.vogella.nattable.parts;

import java.util.List;

import jakarta.annotation.PostConstruct;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultGridLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnLabelAccumulator;
import org.eclipse.swt.widgets.Composite;

import com.vogella.tasks.model.Task;
import com.vogella.tasks.model.TaskService;

public class NatTableAdvancedExample {

	@PostConstruct
	public void postConstruct(Composite parent, TaskService taskService) {
		List<Task> tasks = taskService.getAll();
		IColumnPropertyAccessor<Task> columnPropertyAccessor = new TaskColumnPropertyAccessor();
		ListDataProvider<Task> dataProvider = new ListDataProvider<>(tasks, columnPropertyAccessor);

		TaskHeaderDataProvider headerDataProvider = new TaskHeaderDataProvider();

		DefaultGridLayer gridLayer = new DefaultGridLayer(dataProvider, headerDataProvider);

		ColumnLabelAccumulator columnLabelAccumulator = new ColumnLabelAccumulator(dataProvider);
		((DataLayer) gridLayer.getBodyDataLayer()).setConfigLabelAccumulator(columnLabelAccumulator);

		NatTable natTable = new NatTable(parent, gridLayer, false);
		natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
		natTable.addConfiguration(new EditConfiguration());

		natTable.configure();
	}

}