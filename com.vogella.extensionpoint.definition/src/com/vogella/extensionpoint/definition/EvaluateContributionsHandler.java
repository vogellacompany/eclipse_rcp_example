package com.vogella.extensionpoint.definition;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.e4.core.di.annotations.Execute;

public class EvaluateContributionsHandler {
	private static final String IGREETER_ID = 
			"com.vogella.extensionpoint.definition.greeter";
	@Execute
	public void execute(IExtensionRegistry registry) {
		evaluate(registry);
	}
	
	private void evaluate(IExtensionRegistry registry) {
		IConfigurationElement[] config =
				registry.getConfigurationElementsFor(IGREETER_ID);
		try {
			for (IConfigurationElement e : config) {
				System.out.println("Evaluating extension");
				final Object o =
						e.createExecutableExtension("class");
				if (o instanceof IGreeter) {
					executeExtension(o);
				}
			}
		} catch (CoreException ex) {
			System.out.println(ex.getMessage());
		}
	}

	private void executeExtension(final Object o) {
		ISafeRunnable runnable = new ISafeRunnable() {
			@Override
			public void handleException(Throwable e) {
				System.out.println("Exception in client");
			}

			@Override
			public void run() throws Exception {
				((IGreeter) o).greet();
			}
		};
		SafeRunner.run(runnable);
	}
}
