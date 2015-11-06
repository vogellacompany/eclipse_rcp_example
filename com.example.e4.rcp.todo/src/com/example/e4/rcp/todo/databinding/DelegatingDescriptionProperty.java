package com.example.e4.rcp.todo.databinding;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.property.value.DelegatingValueProperty;
import org.eclipse.core.databinding.property.value.IValueProperty;

import com.example.e4.rcp.todo.model.Todo;

public class DelegatingDescriptionProperty extends DelegatingValueProperty {

	@Override
	protected IValueProperty doGetDelegate(Object source) {
		if(source instanceof Todo) {
			return BeanProperties.value(Todo.FIELD_DESCRIPTION);
		}
		return null;
	}

}
