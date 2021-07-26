package com.example.e4.renderer.swt;

import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.MUILabel;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.renderers.swt.StackRenderer;
import org.eclipse.swt.custom.CTabItem;

public class MyStackRenderer extends StackRenderer {

    private String getLabel(MUILabel itemPart, String newName) {
        if (newName == null) {
            newName = ""; //$NON-NLS-1$
        }
        if (itemPart instanceof MDirtyable && ((MDirtyable) itemPart).isDirty()) {
            newName = '*' + newName; //$NON-NLS-1$
        }
        return newName;
    }

    @Override
	protected void updateTab(CTabItem cti, MPart part, String attName,
            Object newValue) {
        if (UIEvents.UILabel.LABEL.equals(attName)) {
            String newName = (String) newValue;
            cti.setText(getLabel(part, newName));
        } else if (UIEvents.UILabel.ICONURI.equals(attName)) {
            cti.setImage(getImage(part));
        } else if (UIEvents.UILabel.TOOLTIP.equals(attName)) {
            String newTTip = (String) newValue;
            cti.setToolTipText(newTTip);
        } else if (UIEvents.Dirtyable.DIRTY.equals(attName)) {
            Boolean dirtyState = (Boolean) newValue;
            String text = cti.getText();
            boolean hasExclamationMark = text.length() > 0 && text.charAt(0) == '!';
            if (dirtyState.booleanValue()) {
                if (!hasExclamationMark) {
                    cti.setText("* Demo *" + text);
                }
            } else if (hasExclamationMark) {
                cti.setText(text.substring(1));
            }
        }
    }
}