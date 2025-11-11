package com.vogella.tasks.ui.parts;

import static org.eclipse.jface.layout.GridDataFactory.fillDefaults;
import static org.eclipse.jface.widgets.WidgetFactory.button;
import static org.eclipse.jface.widgets.WidgetFactory.text;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.SimpleContentProposalProvider;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.nebula.widgets.chips.Chips;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import jakarta.annotation.PostConstruct;

public class PlaygroundPart {
	private Text text;
	private Browser browser;

	@PostConstruct
	public void createControls(Composite parent) {
		parent.setLayout(new GridLayout(2, false));
		Chips chip1 = new Chips(parent, SWT.CLOSE);
		chip1.setText("Example");
		chip1.setChipsBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		text = text(SWT.BORDER | SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL).message("Enter City")
				.layoutData(fillDefaults().grab(true, false).create()).create(parent);
		text.addSelectionListener(SelectionListener.widgetDefaultSelectedAdapter(e -> updateBrowser()));

		ContentProposalAdapter contentProposal = new ContentProposalAdapter(text, new TextContentAdapter(),
				new SimpleContentProposalProvider("Hamburg", "New York", "New Delhi"), null, null);

		contentProposal.setPopupSize(new Point(200, 100));
		contentProposal.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
		button(SWT.PUSH).text("Search").onSelect(e -> updateBrowser()).create(parent);

		browser = new Browser(parent, SWT.NONE);
		browser.setLayoutData(fillDefaults().grab(true, true).span(2, 1).create());
	}

	private void updateBrowser() {
		String city = text.getText();
		if (city.isEmpty()) {
			return;
		}
		try {
			browser.setUrl("https://www.google.com/maps/place/" + URLEncoder.encode(city, "UTF-8") + "/&output=embed");

		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
	}

	@Focus
	public void onFocus() {
		text.setFocus();
	}
}