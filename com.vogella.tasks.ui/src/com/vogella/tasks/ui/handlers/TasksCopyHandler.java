package com.vogella.tasks.ui.handlers;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.PlainMessageDialog;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.RTFTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.vogella.tasks.model.Task;

public class TasksCopyHandler {

	@Execute
	public void execute(Shell shell, @Optional @Named(IServiceConstants.ACTIVE_SELECTION) List<Task> tasks) {

		Clipboard clipboard = new Clipboard(shell.getDisplay());

		String transferString = tasks.stream().map(task -> task.toString()).collect(Collectors.joining(", "));

		TextTransfer textTransfer = TextTransfer.getInstance();
		RTFTransfer rtfTransfer = RTFTransfer.getInstance();
		LocalSelectionTransfer localSelectionTransfer = LocalSelectionTransfer.getTransfer();

		Object[] transferData = new Object[] { transferString, transferString, tasks };
		Transfer[] transfers = new Transfer[] { textTransfer, rtfTransfer, localSelectionTransfer };

		clipboard.setContents(transferData, transfers);

		clipboard.dispose();

		PlainMessageDialog.getBuilder(shell, "Copied to clipboard").message("Selected task copied to clipboard. Paste in any editor")
				.buttonLabels(List.of("Close")).build().open();

	}

	@CanExecute
	public boolean canExecute(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) List<Task> tasks) {
		return tasks != null && !tasks.isEmpty();
	}
}