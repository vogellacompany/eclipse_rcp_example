package com.vogella.nattable.parts;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ReflectiveColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultRowHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.CornerLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.RowHeaderLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.vogella.model.person.Person;
import com.vogella.model.person.PersonService;

public class NatTableGridExamplePart {

    @PostConstruct
    public void postConstruct(Composite parent, PersonService personService) {
        parent.setLayout(new GridLayout());

        // property names of the Person class
        String[] propertyNames = { 
                "firstName", 
                "lastName", 
                "gender", 
                "married", 
                "birthday" };

		Map<String, String> propertyToLabelMap = Map.of("firstName", "Firstname", "lastName", "Lastname", "gender",
				"Gender", "married", "Married", "birthday", "Birthday");

        IColumnPropertyAccessor<Person> columnPropertyAccessor = 
				new ReflectiveColumnPropertyAccessor<>(propertyNames);

        // build the body layer stack
        IDataProvider bodyDataProvider = 
                new ListDataProvider<Person>(
                        personService.getPersons(50),
                        columnPropertyAccessor);
        DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);
        SelectionLayer selectionLayer = new SelectionLayer(bodyDataLayer);
        ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);

        // build the column header layer stack
        IDataProvider columnHeaderDataProvider = 
                new DefaultColumnHeaderDataProvider(propertyNames, propertyToLabelMap);
        DataLayer columnHeaderDataLayer = 
                new DataLayer(columnHeaderDataProvider);
        ILayer columnHeaderLayer = 
                new ColumnHeaderLayer(columnHeaderDataLayer, viewportLayer, selectionLayer);

        // build the row header layer stack
        IDataProvider rowHeaderDataProvider = 
                new DefaultRowHeaderDataProvider(bodyDataProvider);
        DataLayer rowHeaderDataLayer = 
                new DataLayer(rowHeaderDataProvider, 40, 20);
        ILayer rowHeaderLayer = 
                new RowHeaderLayer(rowHeaderDataLayer, viewportLayer, selectionLayer);

        // build the corner layer stack
        ILayer cornerLayer = new CornerLayer(
                new DataLayer(
                        new DefaultCornerDataProvider(columnHeaderDataProvider, rowHeaderDataProvider)),
                rowHeaderLayer, 
                columnHeaderLayer);

        // create the grid layer composed with the prior created layer stacks
        GridLayer gridLayer = 
                new GridLayer(viewportLayer, columnHeaderLayer, rowHeaderLayer, cornerLayer);

        NatTable natTable = new NatTable(parent, gridLayer);

        GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);
    }

}