package com.vogella.tasks.ui.parts;

import static org.eclipse.jface.layout.GridDataFactory.fillDefaults;
import static org.eclipse.jface.widgets.WidgetFactory.button;
import static org.eclipse.jface.widgets.WidgetFactory.text;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.SimpleContentProposalProvider;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class PlaygroundPart {
	private Text text;
	private Browser browser;

	@PostConstruct
	public void createControls(Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		text = text(SWT.BORDER | SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL).message("Enter City")
				.layoutData(fillDefaults().grab(true, false).create()).create(parent);
		text.addSelectionListener(SelectionListener.widgetDefaultSelectedAdapter(e -> updateBrowser()));

		ContentProposalAdapter contentProposal = new ContentProposalAdapter(text, new TextContentAdapter(),
				new SimpleContentProposalProvider("Hamburg", "New York", "New Delhi"), null, null);
		addControlDecoration();
		contentProposal.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
		button(SWT.PUSH).text("Search").onSelect(e -> updateBrowser()).create(parent);

		browser = new Browser(parent, SWT.NONE);
		browser.setLayoutData(fillDefaults().grab(true, true).span(2, 1).create());

	}

	private void addControlDecoration() {
		// create the decoration for the text component
		// using an predefined image
		ControlDecoration deco = new ControlDecoration(text, SWT.TOP | SWT.LEFT);
		Image image = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION)
				.getImage();
		// set description and image
		deco.setDescriptionText("Use CTRL + SPACE to see possible values");
		deco.setImage(image);
		// always show decoration
		deco.setShowOnlyOnFocus(false);

		// hide the decoration if the text widget has content
		text.addModifyListener(e -> {
			Text source = (Text) e.getSource();
			if (!source.getText().isEmpty()) {
				deco.hide();
			} else {
				deco.show();
			}
		});

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