package com.vogella.nattable.parts;

import org.eclipse.nebula.widgets.nattable.data.IDataProvider;

public class TaskHeaderDataProvider implements IDataProvider {

	@Override
	public Object getDataValue(int columnIndex, int rowIndex) {
		switch (columnIndex) {

		case 0:
			return "ID";
		case 1:
			return "Summary";
		case 2:
			return "Description";
		case 3:
			return "Done";
		case 4:
			return "Due Date";
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