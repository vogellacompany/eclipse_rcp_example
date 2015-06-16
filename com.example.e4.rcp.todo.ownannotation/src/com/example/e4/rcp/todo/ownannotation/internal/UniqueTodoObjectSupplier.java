package com.example.e4.rcp.todo.ownannotation.internal;

import java.util.Date;

import org.eclipse.e4.core.di.suppliers.ExtendedObjectSupplier;
import org.eclipse.e4.core.di.suppliers.IObjectDescriptor;
import org.eclipse.e4.core.di.suppliers.IRequestor;

import com.example.e4.rcp.todo.model.Todo;


public class UniqueTodoObjectSupplier extends ExtendedObjectSupplier {
@Override
public Object get(IObjectDescriptor descriptor, IRequestor requestor,
         boolean track, boolean group) {
      System.out.println("Own annotation processor");
      // Just for the purpose of this example
      // return a hard-coded Todo
      // You could add checks which makes this Todo unique, e.g. 
      // be access the TodoService and reading all existing ID's
      Todo todo = new Todo(42, "Checked", "Checked", false, new Date());
      return todo;
   }
} 