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

import com.vogella.model.person.Person;
import com.vogella.model.person.PersonService;

public class NatTableLayerStackExamplePart {

	@PostConstruct
	public void postConstruct(Composite parent, PersonService personService) {

		parent.setLayout(new GridLayout());

		// create the data provider
		IColumnPropertyAccessor<Person> columnPropertyAccessor = new PersonColumnPropertyAccessor();
		IDataProvider bodyDataProvider = new ListDataProvider<Person>(personService.getPersons(50),
				columnPropertyAccessor);

		// build up a layer stack consisting of DataLayer, SelectionLayer and
		// ViewportLayer
		DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);
		SelectionLayer selectionLayer = new SelectionLayer(bodyDataLayer);
		// Enable full selection for rows
		selectionLayer.addConfiguration(new DefaultRowSelectionLayerConfiguration());

		ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);

		// as the selection mouse bindings are registered for the region label
		// GridRegion.BODY
		// we need to set that region label to the viewport so the selection via mouse
		// is working correctly
		viewportLayer.setRegionName(GridRegion.BODY);

		NatTable natTable = new NatTable(parent, viewportLayer,false);


		GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);
	}
}