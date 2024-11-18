package com.example.e4.rcp.handlers;

import java.lang.reflect.Field;
import java.util.Properties;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class AboutHandler {
	@Execute
	public void execute(Shell shell) {
		try {
			// Create an instance of Properties
			Properties properties = new Properties();
			properties.setProperty("key1", "value1");

			// Access the private field 'defaults'
			Field defaultsField = Properties.class.getDeclaredField("defaults");

			// Set the field accessible
			defaultsField.setAccessible(true);

			// Get the value of the private field
			Properties defaults = (Properties) defaultsField.get(properties);

			// Output the value of the defaults field
			System.out.println("Defaults: " + defaults);

			// Cleanup: Reset the accessibility (optional)
			defaultsField.setAccessible(false);
			if (MessageDialog.openConfirm(shell, "Confirmation", "Defaults: " + defaults)) {
			}

		} catch (Exception e) {

			MessageDialog.openInformation(shell, "Confirmation", "" + e.getLocalizedMessage());
		}
	}
}
