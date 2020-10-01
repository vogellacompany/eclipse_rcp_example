package com.vogella.nattable.parts;

import org.eclipse.nebula.widgets.nattable.data.IDataProvider;

public class PersonHeaderDataProvider implements IDataProvider {

	@Override
	public Object getDataValue(int columnIndex, int rowIndex) {
		switch (columnIndex) {

		case 0:
			return "First Name";
		case 1:
			return "Last Name";
		case 2:
			return "Gender";
		case 3:
			return "Married";
		case 4:
			return "Birthday";
		}
		return "";
	}
	
	@Override
	public int getColumnCount() {
		return 5;
	}
	
	@Override
	public int getRowCount() {
		return 1;
	}

	@Override
	public void setDataValue(int columnIndex, int rowIndex, Object newValue) {
		throw new UnsupportedOperationException("Setting data values to the header is not supported");
		
	}

}