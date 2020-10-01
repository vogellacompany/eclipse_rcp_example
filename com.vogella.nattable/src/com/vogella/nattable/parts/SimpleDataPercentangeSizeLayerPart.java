package com.vogella.nattable.parts;

import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.swt.widgets.Composite;

import com.vogella.model.person.Person;
import com.vogella.model.person.PersonService;

public class SimpleDataPercentangeSizeLayerPart {

	@PostConstruct
	public void postConstruct(Composite parent, PersonService personService) {

		List<Person> persons = personService.getPersons(10);
		PersonColumnPropertyAccessor columnPropertyAccessor = new PersonColumnPropertyAccessor();
		ListDataProvider<Person> dataProvider = new ListDataProvider<>(persons, columnPropertyAccessor);
		DataLayer bodyDataLayer = new DataLayer(dataProvider);

		// activate percentage sizing for all columns
		bodyDataLayer.setColumnPercentageSizing(true);

		// Use percentage values for the first name, last name and gender column
		bodyDataLayer.setColumnWidthPercentageByPosition(0, 20);
		bodyDataLayer.setColumnWidthPercentageByPosition(1, 20);
		bodyDataLayer.setColumnWidthPercentageByPosition(2, 20);

		// deactivate percentage sizing for the married column
		bodyDataLayer.setColumnPercentageSizing(3, false);
		// apply a fixed size for the married column
		bodyDataLayer.setColumnWidthByPosition(3, 50);
		
		// also use percentage sizing for the birthday column
		bodyDataLayer.setColumnWidthPercentageByPosition(4, 40);
		
		new NatTable(parent, bodyDataLayer);
	}
}
