package com.example.e4.rcp.todo.databinding;

import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.property.list.DelegatingListProperty;
import org.eclipse.core.databinding.property.list.IListProperty;

import com.example.e4.rcp.todo.model.Tag;

public class TagTreeListProperty extends DelegatingListProperty {

    @Override
    protected IListProperty doGetDelegate(Object source) {
        if (source instanceof Tag) {
            return PojoProperties.list(Tag.TAGGEDELEMENTS_FIELD);
        } else {
            return null;
        }
    }

}
