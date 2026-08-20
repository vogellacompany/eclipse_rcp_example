package com.vogella.tasks.update.handlers;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Dialog to specify an update repository location. The location can be either
 * a URL (e.g. {@code http://example.com/repository}) or a local file system
 * path chosen via a directory browser.
 */
public class RepositoryLocationDialog extends TitleAreaDialog {

	private Text locationText;
	private String location;

	/**
	 * Creates the dialog.
	 *
	 * @param parentShell     the parent shell
	 * @param initialLocation the pre-filled location (may be {@code null} or empty)
	 */
	public RepositoryLocationDialog(Shell parentShell, String initialLocation) {
		super(parentShell);
		this.location = initialLocation != null ? initialLocation : "";
	}

	@Override
	public void create() {
		super.create();
		setTitle("Update Repository Location");
		setMessage(
				"Enter a URL or browse for a local directory that contains the update repository.",
				IMessageProvider.INFORMATION);
		validate();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);

		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(3, false);
		layout.marginWidth = 10;
		layout.marginHeight = 10;
		container.setLayout(layout);

		Label locationLabel = new Label(container, SWT.NONE);
		locationLabel.setText("Repository location:");

		locationText = new Text(container, SWT.BORDER | SWT.SINGLE);
		locationText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		locationText.setText(location);
		locationText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				location = locationText.getText().trim();
				validate();
			}
		});

		Button browseButton = new Button(container, SWT.PUSH);
		browseButton.setText("Browse...");
		browseButton.addListener(SWT.Selection, event -> {
			DirectoryDialog directoryDialog = new DirectoryDialog(getShell(), SWT.OPEN);
			directoryDialog.setText("Select Repository Directory");
			directoryDialog.setMessage("Select the local directory that contains the update repository:");
			if (!location.isEmpty() && !location.startsWith("http")) {
				directoryDialog.setFilterPath(location);
			}
			String selectedPath = directoryDialog.open();
			if (selectedPath != null) {
				locationText.setText(selectedPath);
			}
		});

		Label hintLabel = new Label(container, SWT.WRAP);
		hintLabel.setText("Hint: Enter a URL (e.g. https://example.com/repository) or use \"Browse...\" to select a local folder.");
		GridData hintData = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		hintData.widthHint = 400;
		hintLabel.setLayoutData(hintData);

		return area;
	}

	/**
	 * Validates the current input and enables/disables the OK button accordingly.
	 */
	private void validate() {
		if (location == null || location.isEmpty()) {
			setErrorMessage("Please enter a repository location.");
			getButton(OK).setEnabled(false);
		} else {
			setErrorMessage(null);
			getButton(OK).setEnabled(true);
		}
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Update Repository");
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	/**
	 * Returns the repository location entered by the user.
	 *
	 * @return the repository location string (never {@code null})
	 */
	public String getLocation() {
		return location;
	}
}
