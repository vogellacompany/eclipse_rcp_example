package com.vogella.tasks.ui.parts;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.eclipse.swt.widgets.Composite;

import com.vogella.tasks.model.ITodoService;
import com.vogella.tasks.model.Todo;

public class TodoOverviewPart {
    @PostConstruct
    public void createControls(Composite parent, ITodoService todoService) {
        todoService.getTodos(todos -> {
            System.out.println("Number of Todo objects " + todos.size());
        });
		var todos = new ArrayList<Todo>();
		todoService.getTodos(todos::addAll);
		var todo = todos.get(0);
		todo.setDescription("Hello");
		todoService.saveTodo(todo);

    }
}