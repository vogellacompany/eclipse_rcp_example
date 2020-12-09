package com.vogella.nattable.parts;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;

import com.vogella.tasks.model.Task;

public class TaskColumnPropertyAccessor implements IColumnPropertyAccessor<Task> {

    // property names of the Task class
    // will also be used for the column header once you define them
	public static final List<String> propertyNames =
			Arrays.asList(Task.FIELD_ID, Task.FIELD_SUMMARY, Task.FIELD_DESCRIPTION, Task.FIELD_DONE,
					Task.FIELD_DUEDATE);

    @Override
    public Object getDataValue(Task task, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return task.getId();
            case 1:
                return task.getSummary();
            case 2:
                return task.getDescription();
            case 3:
                return task.isDone();
            case 4:
                return task.getDueDate();
            default:
                return "UNDEFINED";
            }
    }

    @Override
    public void setDataValue(Task task, int columnIndex, Object newValue) {
        switch (columnIndex) {
            case 0:
                throw new IllegalArgumentException("change not allowed");
            case 1:
                task.setSummary(String.valueOf(newValue));
                break;
            case 2:
                task.setDescription(String.valueOf(newValue));
            case 3:
                task.setDone((boolean) newValue);
                break;
            case 4:
                String stringDate = (String) newValue;
                LocalDate date = LocalDate.parse(stringDate);
                task.setDueDate(date);
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