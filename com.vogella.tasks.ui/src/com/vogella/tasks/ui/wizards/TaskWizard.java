package com.vogella.tasks.ui.wizards;

import org.eclipse.jface.wizard.Wizard;

import com.vogella.tasks.model.Task;

public class TaskWizard extends Wizard {

    private Task task;

    public TaskWizard(Task todo) {
        this.task = todo;
        setWindowTitle("New Wizard");
    }

    @Override
    public void addPages() {
        addPage(new CreateTaskPage1(task));
        addPage(new CreateTaskPage2());
    }

    @Override
    public boolean performFinish() {
        return true;
    }

}