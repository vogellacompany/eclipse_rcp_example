package com.vogella.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public class HighlightComposite extends Composite {

	private static Color regularColor;
	private static Color selectionColor;

	private final class MouseTrackAdapterExtension extends MouseTrackAdapter {

		@Override
		public void mouseEnter(MouseEvent e) {
			HighlightComposite.this.setBackground(selectionColor);
		}

		@Override
		public void mouseExit(MouseEvent e) {

			HighlightComposite.this.setBackground(regularColor);
		}
	}

	MouseTrackAdapter mouseTracker = new MouseTrackAdapterExtension();

	public HighlightComposite(final Composite parent, int style) {
		super(parent, style);
		regularColor = parent.getBackground();
		selectionColor = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
		
		GridData gdHigh = new GridData(GridData.FILL);
		this.setLayoutData(gdHigh);
		
		setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GREEN));
		GridLayout layHigh = new GridLayout(2, false);
		layHigh.marginHeight = 0;
		layHigh.marginWidth = 0;
		layHigh.horizontalSpacing = 0;
		HighlightComposite.this.setLayout(layHigh);

		addMouseTrackListener(mouseTracker);
	}

	public void addMouseTrackListener() {
		for (Control c : super.getChildren()) {
			c.addMouseTrackListener(mouseTracker);
		}
	}
}
