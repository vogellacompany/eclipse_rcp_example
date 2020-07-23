package com.vogella.tasks.ui.parts;

import javax.annotation.PostConstruct;

import org.eclipse.swt.widgets.Composite;

import com.vogella.tasks.model.ITodoService;

public class TodoOverviewPart {
    @PostConstruct
    public void createControls(Composite parent, ITodoService todoService) {
        todoService.getTodos(todos -> {
            System.out.println("Number of Todo objects " + todos.size());
        });
    }
}