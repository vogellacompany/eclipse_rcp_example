package com.example.e4.rcp.todo.toolcontrols;

import javax.inject.Inject;

import org.eclipse.e4.core.services.nls.ILocaleChangeService;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.example.e4.rcp.todo.i18n.Messages;

public class SwitchLanguageToolControl {

	Button button;

	@Inject
	ILocaleChangeService lcs;

	@Inject
	public SwitchLanguageToolControl(Composite parent) {

		final Text input = new Text(parent, SWT.BORDER);

		input.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.keyCode == SWT.CR || event.keyCode == SWT.KEYPAD_CR) {
					lcs.changeApplicationLocale(input.getText());
				}
			}
		});

		button = new Button(parent, SWT.PUSH);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				lcs.changeApplicationLocale(input.getText());
			};
		});
	}

	@Inject
	public void translate(@Translation Messages messages) {
		// button localization via Eclipse Translation Pattern
		if (button != null && !button.isDisposed()) {
			button.setText(messages.toolbar_main_changelocale);
		}
	}
}