/*******************************************************************************
 * Copyright (c) 2010 - 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Lars Vogel <lars.Vogel@gmail.com> - Bug 419770
 *******************************************************************************/
package com.vogella.e4.selectionservice.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ListenerPart {

	private Label labelSelection;

	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		labelSelection = new Label(parent, SWT.NONE);
		labelSelection.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
				false, false));
		labelSelection.setText("Initial test");
	}

	@Inject
	@Optional
	public void setSelection(
			@Named("myselection") String selection) {
		if (labelSelection != null && !labelSelection.isDisposed()) {
			labelSelection.setText(selection);
		}
	}

}