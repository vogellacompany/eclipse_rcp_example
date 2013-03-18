package com.example.e4.rcp.todo.addons;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.equinox.app.IApplicationContext;

public class SetPerspectiveAddon {

	private String[] args;

	@PostConstruct
	public void setPerspective(IApplicationContext context) {
		System.out.println("I'm called");
		args = (String[]) context.getArguments().get(
				IApplicationContext.APPLICATION_ARGS);
		for (Object value : args) {
			System.out.println(value);
		}
		String perspective = getArgValue("perspective", context, false);
		System.out.println(perspective);
		// TODO Inject ModelService and PartServise and set the perspective
	}
	
	private String getArgValue(String argName, IApplicationContext appContext,
			boolean singledCmdArgValue) {
		// Is it in the arg list ?
		if (argName == null || argName.length() == 0)
			return null;

		if (singledCmdArgValue) {
			for (int i = 0; i < args.length; i++) {
				if (("-" + argName).equals(args[i]))
					return "true";
			}
		} else {
			for (int i = 0; i < args.length; i++) {
				if (("-" + argName).equals(args[i]) && i + 1 < args.length)
					return args[i + 1];
			}
		}

		final String brandingProperty = appContext.getBrandingProperty(argName);
		return brandingProperty == null ? System.getProperty(argName)
				: brandingProperty;
	}

}
