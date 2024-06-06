package com.vogella.contribute.parts;

import jakarta.annotation.PostConstruct;

import org.eclipse.jface.widgets.TextFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class AdditionalInformationPart {
    @PostConstruct
    public void postConstruct(Composite parent) {
        TextFactory.newText(SWT.BORDER |SWT.MULTI).create(parent);
    }
}