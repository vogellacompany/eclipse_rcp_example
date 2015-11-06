package com.example.e4.rcp.todo.databinding;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.property.value.DelegatingValueProperty;
import org.eclipse.core.databinding.property.value.IValueProperty;

import com.example.e4.rcp.todo.model.Tag;
import com.example.e4.rcp.todo.model.Todo;

public class DelegatingLabelProperty extends DelegatingValueProperty {

	@Override
	protected IValueProperty doGetDelegate(Object source) {
		if(source instanceof Tag) {
			return PojoProperties.value(Tag.LABEL_FIELD);
		} else if (source instanceof Todo) {
			return BeanProperties.value(Todo.class, Todo.FIELD_SUMMARY);
		}
		return null;
	}

}
