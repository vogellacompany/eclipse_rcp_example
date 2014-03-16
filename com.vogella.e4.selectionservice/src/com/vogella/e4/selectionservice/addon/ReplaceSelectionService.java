package com.vogella.e4.selectionservice.addon;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.osgi.service.event.Event;

import com.vogella.e4.selectionservice.service.MyContextFunction;
import com.vogella.e4.selectionservice.service.MySelectionService;

public class ReplaceSelectionService {

	@PostConstruct
	public void replace(MApplication app) {
		IEclipseContext appContext = app.getContext();
		appContext.set(MySelectionService.class.getName(),
				new MyContextFunction());
	}

}
