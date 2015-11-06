package com.example.e4.rcp.todo.databinding;

import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.jface.databinding.viewers.ObservableMapCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

public class DelegatingObservableCellLabelProvider extends ObservableMapCellLabelProvider {

	private IValueProperty valueProperty;

	public DelegatingObservableCellLabelProvider(IObservableSet attributeMap, IValueProperty valueProperty) {
		super(valueProperty.observeDetail(attributeMap));
		this.valueProperty = valueProperty;
	}

	@Override
	public void update(ViewerCell cell) {
		Object value = valueProperty.getValue(cell.getElement());
		cell.setText(value == null ? "" : value.toString()); //$NON-NLS-1$
	}

}
