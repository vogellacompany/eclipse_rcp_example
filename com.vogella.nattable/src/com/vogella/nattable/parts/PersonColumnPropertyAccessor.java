package com.vogella.nattable.parts;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;

import com.vogella.model.person.Person;
import com.vogella.model.person.Person.Gender;

public class PersonColumnPropertyAccessor  implements IColumnPropertyAccessor<Person> {

    private static final List<String> propertyNames = 
        Arrays.asList("firstName", "lastName", "gender", "married", "birthday");

    @Override
    public Object getDataValue(Person person, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return person.getFirstName();
            case 1:
                return person.getLastName();
            case 2:
                return person.getGender();
            case 3:
                return person.isMarried();
            case 4:
                return person.getBirthday();
			default:
				return "UNDEFINED";
            }
    }

    @Override
    public void setDataValue(Person person, int columnIndex, Object newValue) {
        switch (columnIndex) {
            case 0:
                String firstName = String.valueOf(newValue);
                person.setFirstName(firstName);
                break;
            case 1:
                String lastName = String.valueOf(newValue);
                person.setLastName(lastName);
                break;
            case 2:
                person.setGender((Gender) newValue);
                break;
            case 3:
                person.setMarried((boolean) newValue);
                break;
            case 4:
                person.setBirthday((Date) newValue);
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