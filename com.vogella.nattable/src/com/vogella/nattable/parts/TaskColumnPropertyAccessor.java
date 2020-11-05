package com.vogella.nattable.parts;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;

import com.vogella.tasks.model.Task;

public class TaskColumnPropertyAccessor  implements IColumnPropertyAccessor<Task> {

    private static final List<String> propertyNames = 
        Arrays.asList("summary", "description", "dueDate", "done");

    @Override
    public Object getDataValue(Task task, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return  task.getSummary();
            case 1:
                return task.getDescription();
            case 2:
                return task.getDueDate();
            case 3:
                return task.isDone();
			default:
				return "UNDEFINED";
            }
    }

    @Override
    public void setDataValue(Task task, int columnIndex, Object newValue) {
        switch (columnIndex) {
            case 0:
                String summary = String.valueOf(newValue);
                task.setSummary(summary);
                break;
            case 1:
                String description = String.valueOf(newValue);
                task.setDescription(description);
                break;
            case 2:
            	task.setDueDate((LocalDate) newValue);
                break;
            case 4:
            	String value =  String.valueOf(newValue);
            	task.setDone(Boolean.parseBoolean(value));
                break;
			default:
				throw new IllegalArgumentException("column number out of range");

        }
    }

    @Override
    public int getColumnCount() {
		return propertyNames.size();
    }

    @Override
    public String getColumnProperty(int columnIndex) {
        return propertyNames.get(columnIndex);
    }

    @Override
    public int getColumnIndex(String propertyName) {
        return propertyNames.indexOf(propertyName);
    }

}