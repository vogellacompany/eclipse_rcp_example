package com.vogella.swt.widgets;


import org.eclipse.jface.widgets.WidgetFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class LabelWithText extends Composite {

	public LabelWithText(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(2, false);
		this.setLayout(gridLayout);
		WidgetFactory.label(SWT.BORDER).create(this);
		WidgetFactory.button(SWT.PUSH).create(this);

	}


}
