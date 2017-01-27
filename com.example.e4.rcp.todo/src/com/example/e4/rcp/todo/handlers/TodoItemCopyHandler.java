
package com.example.e4.rcp.todo.handlers;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.RTFTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;

import com.example.e4.rcp.todo.model.Todo;

@SuppressWarnings("restriction")
public class TodoItemCopyHandler {

	@Execute
	public void execute(Logger log, Display display,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) List<Todo> selectedTodos) {
		if (selectedTodos == null || selectedTodos.isEmpty()) {
			log.info("There are no selected todos");
			return;
		}

		Clipboard clipboard = new Clipboard(display);

		String transferString = selectedTodos.stream().map(todo -> todo.toString()).collect(Collectors.joining(", "));

		TextTransfer textTransfer = TextTransfer.getInstance();
		RTFTransfer rtfTransfer = RTFTransfer.getInstance();
		LocalSelectionTransfer localSelectionTransfer = LocalSelectionTransfer.getTransfer();

		Object[] transferData = new Object[] { transferString, transferString, selectedTodos };
		Transfer[] transfers = new Transfer[] { textTransfer, rtfTransfer, localSelectionTransfer };

		clipboard.setContents(transferData, transfers);

		clipboard.dispose();
	}

	@CanExecute
	public boolean canExecute(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) List<Todo> selectedTodos) {
		return selectedTodos != null && !selectedTodos.isEmpty();
	}
}
