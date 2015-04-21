package com.example.e4.rcp.todo.addons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ElementMatcher;
import org.eclipse.e4.ui.workbench.modeling.IWindowCloseHandler;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

// @PostConstruct does not work as the workbench gets 
// instantiated after the processing of the add-ons
// hence this approach uses method injection

public class WindowCloseListenerAddon {

	@Inject
	@Optional
	private void subscribeApplicationCompleted(
			@UIEventTopic(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE) final MApplication application,
			final IWorkbench workbench) {
		// only if a close of the main window is requested
		MWindow mainWindow = findMainWindow(application);

		mainWindow.getContext().set(IWindowCloseHandler.class, new IWindowCloseHandler() {
			@Override
			public boolean close(MWindow window) {
				Collection<EPartService> allPartServices = getAllPartServices(application);

				if (containsDirtyParts(allPartServices)) {
					Shell shell = (Shell) window.getWidget();
					MessageDialog msgDialog = new MessageDialog(shell, "Close Application", null,
							"Do you want to save the changes before you close the entire application?",
							MessageDialog.QUESTION_WITH_CANCEL, new String[] { IDialogConstants.YES_LABEL,
									IDialogConstants.NO_LABEL, IDialogConstants.CANCEL_LABEL },
							0);
					switch (msgDialog.open()) {
					case 0: // YES: save all and close
						saveAll(allPartServices);
						return workbench.close();

					case 1: // NO: save nothing and close
						return workbench.close();

					case 2: // CANCEL: prevent close
					default:
						return false;
					}
				} else {
					Shell shell = (Shell) window.getWidget();
					if (MessageDialog.openConfirm(shell, "Close Application",
							"Do you really want to close the entire application?")) {
						return workbench.close();
					}
				}

				return false;
			}
		});
	}

	private static MWindow findMainWindow(MApplication application) {
		// instead of using the index you could also use a tag on the MWindow to
		// mark the main window
		return application.getChildren().get(0);
	}

	private static Collection<EPartService> getAllPartServices(MApplication application) {
		List<EPartService> partServices = new ArrayList<EPartService>();

		EModelService modelService = application.getContext().get(EModelService.class);
		List<MWindow> elements = modelService.findElements(application, MWindow.class, EModelService.IN_ACTIVE_PERSPECTIVE,
				new ElementMatcher(null, MWindow.class, (List<String>) null));
		for (MWindow w : elements) {
			if (w.isVisible() && w.isToBeRendered()) {
				EPartService partService = w.getContext().get(EPartService.class);
				if (partService != null) {
					partServices.add(partService);
				}
			}
		}

		return partServices;
	}

	private static boolean containsDirtyParts(Collection<EPartService> partServices) {
		for (EPartService partService : partServices) {
			if (!partService.getDirtyParts().isEmpty())
				return true;
		}

		return false;
	}

	private static void saveAll(Collection<EPartService> partServices) {
		for (EPartService partService : partServices) {
			partService.saveAll(false); // false: save without prompt
		}
	}
}