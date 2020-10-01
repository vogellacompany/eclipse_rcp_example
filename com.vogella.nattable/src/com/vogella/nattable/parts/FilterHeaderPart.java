package com.vogella.nattable.parts;

import javax.annotation.PostConstruct;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.ConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.edit.config.DefaultEditConfiguration;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.filterrow.DefaultGlazedListsFilterStrategy;
import org.eclipse.nebula.widgets.nattable.filterrow.FilterRowHeaderComposite;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultColumnHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.layer.CompositeLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.widgets.Composite;

import com.vogella.model.person.Person;
import com.vogella.model.person.PersonService;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;

public class FilterHeaderPart {

	@PostConstruct
	public void postConstruct(Composite parent, PersonService personService) {

		ConfigRegistry configRegistry = new ConfigRegistry();

		// build the body region
		EventList<Person> persons = GlazedLists.threadSafeList(GlazedLists.eventList(personService.getPersons(10)));
		SortedList<Person> sortedList = new SortedList<>(persons, null);
		FilterList<Person> filterList = new FilterList<>(sortedList);

		IColumnPropertyAccessor<Person> accessor = new PersonColumnPropertyAccessor();
		IDataProvider bodyDataProvider = new ListDataProvider<>(filterList, accessor);
		DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);

		SelectionLayer selectionLayer = new SelectionLayer(bodyDataLayer);
		ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);

		// build the column header region
		IDataProvider columnHeaderDataProvider = new DefaultColumnHeaderDataProvider(
				personService.getDefaultPropertyNames(), personService.getDefaultPropertyToLabelMap());
		DataLayer columnHeaderDataLayer = new DefaultColumnHeaderDataLayer(columnHeaderDataProvider);


		ILayer columnHeaderLayer = new ColumnHeaderLayer(columnHeaderDataLayer, viewportLayer, selectionLayer);

		FilterRowHeaderComposite<Person> filterRowHeaderLayer = new FilterRowHeaderComposite<>(
				new DefaultGlazedListsFilterStrategy<>(filterList, accessor, configRegistry), columnHeaderLayer,
				columnHeaderDataLayer.getDataProvider(), configRegistry);

		// create the CompositeLayer, which combines header and body
		CompositeLayer compositeLayer = new CompositeLayer(1, 2);
		compositeLayer.setChildLayer(GridRegion.COLUMN_HEADER, filterRowHeaderLayer, 0, 0);
		compositeLayer.setChildLayer(GridRegion.BODY, viewportLayer, 0, 1);
		// necessary for the usage of editors
		compositeLayer.addConfiguration(new DefaultEditConfiguration());

		NatTable natTable = new NatTable(parent, compositeLayer, false);

		natTable.setConfigRegistry(configRegistry);
		natTable.addConfiguration(new DefaultNatTableStyleConfiguration());

		// apply the filter configurations
		natTable.addConfiguration(new FilterRowConfiguration());

		natTable.configure();
	}
}
