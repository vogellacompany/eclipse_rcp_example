package com.vogella.nattable.parts;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.vogella.model.person.Person;
import com.vogella.model.person.PersonService;

public class NatTableDataExamplePart {

	@PostConstruct
	public void postConstruct(Composite parent, PersonService personService, ESelectionService selectionService) {
		parent.setLayout(new GridLayout());

		// create the data provider
		IColumnPropertyAccessor<Person> columnPropertyAccessor = new PersonColumnPropertyAccessor();
		IRowDataProvider<Person> bodyDataProvider = new ListDataProvider<>(personService.getPersons(10),
				columnPropertyAccessor);

		DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);

//		SelectionLayer selectionLayer = new SelectionLayer(bodyDataLayer);
//		E4SelectionListener<Person> e4SelectionListener = new E4SelectionListener<>(selectionService,
//				selectionLayer,
//				bodyDataProvider);
//		e4SelectionListener.setFullySelectedRowsOnly(false);
//		e4SelectionListener.setHandleSameRowSelection(false);
//		selectionLayer.addLayerListener(e4SelectionListener);

		SelectionLayer selectionLayer = new SelectionLayer(bodyDataLayer);
		ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);

		// as the selection mouse bindings are registered for the region label
		// GridRegion.BODY
		// we need to set that region label to the viewport so the selection via mouse
		// is working correctly
		viewportLayer.setRegionName(GridRegion.BODY);

		NatTable natTable = new NatTable(parent, viewportLayer, false);
		natTable.addConfiguration(new AbstractRegistryConfiguration() {

			@Override
			public void configureRegistry(IConfigRegistry configRegistry) {
				configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE,
						IEditableRule.ALWAYS_EDITABLE);
			}
		});
		GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);
	}
}