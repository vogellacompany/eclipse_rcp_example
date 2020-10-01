package com.vogella.nattable.parts;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.extension.e4.selection.E4SelectionListener;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.vogella.model.person.Person;
import com.vogella.model.person.PersonService;

public class NatTableE4SelectionListenerPart {
	@PostConstruct
	public void postConstruct(Composite parent, PersonService personService, ESelectionService selectionService) {
		parent.setLayout(new GridLayout());

		IRowDataProvider<Person> bodyDataProvider = new ListDataProvider<>(personService.getPersons(10),
				new PersonColumnPropertyAccessor());

		DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);
		SelectionLayer selectionLayer = new SelectionLayer(bodyDataLayer);
		E4SelectionListener<Person> e4SelectionListener = new E4SelectionListener<>(selectionService, selectionLayer,
				bodyDataProvider);
		e4SelectionListener.setFullySelectedRowsOnly(false);
		e4SelectionListener.setHandleSameRowSelection(false);
		selectionLayer.addLayerListener(e4SelectionListener);

		ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);
		viewportLayer.setRegionName(GridRegion.BODY);

		NatTable natTable = new NatTable(parent, viewportLayer);

		GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);
	}
}
