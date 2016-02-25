package com.example.e4.renderer.swt.pshelf;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.MUILabel;
import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.IPresentationEngine;
import org.eclipse.e4.ui.workbench.renderers.swt.LazyStackRenderer;
import org.eclipse.jface.action.LegacyActionTools;
import org.eclipse.nebula.widgets.pshelf.PShelf;
import org.eclipse.nebula.widgets.pshelf.PShelfItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class PShelfStackRenderer extends LazyStackRenderer {

	@Inject
	private IEclipseContext eclipseContext;

	@Inject
	private IEventBroker eventBroker;
	
	@Inject
	private IPresentationEngine renderer;

	@Inject
	public void init() {
		init(eclipseContext);
		init(eventBroker);
	}

	@Override
	public Object createWidget(MUIElement element, Object parent) {
		if (!(element instanceof MPartStack) || !(parent instanceof Composite))
			return null;

		MPartStack pStack = (MPartStack) element;

		Composite parentComposite = (Composite) parent;

		// Ensure that all rendered PartStacks have an Id
		if (element.getElementId() == null || element.getElementId().length() == 0) {
			String generatedId = "PartStack@" + Integer.toHexString(element.hashCode()); //$NON-NLS-1$
			element.setElementId(generatedId);
		}

		int styleOverride = getStyleOverride(pStack);
		int style = styleOverride == -1 ? SWT.BORDER : styleOverride;
		final PShelf pshelf = new PShelf(parentComposite, style);

		bindWidget(element, pshelf); // ?? Do we need this ?

		return pshelf;
	}

	@Override
	protected void createTab(MElementContainer<MUIElement> stack, MUIElement element) {
		MPart part = null;
		if (element instanceof MPart)
			part = (MPart) element;
		else if (element instanceof MPlaceholder) {
			part = (MPart) ((MPlaceholder) element).getRef();
			if (part != null) {
				part.setCurSharedRef((MPlaceholder) element);
			}
		}

		PShelf ctf = (PShelf) stack.getWidget();

		PShelfItem cti = findItemForPart(element, stack);
		if (cti != null) {
			if (element.getWidget() != null && cti.getBody() != element.getWidget())
				return;
		}

		// Create the tab; we may have more visible tabs than currently shown
		// (e.g., a result of calling partStack.getChildren().addAll(partList))
		int index = Math.min(calcIndexFor(stack, element), ctf.getItems().length);
		cti = new PShelfItem(ctf, SWT.NONE, index);

		cti.setData(OWNING_ME, element);
		cti.setText(getLabel(part, part.getLocalizedLabel()));
		cti.setImage(getImage(part));

		String toolTip = getToolTip(part);
		if (toolTip == null)
			toolTip = part.getLocalizedTooltip();
		cti.getBody().setToolTipText(getToolTip(toolTip));
		cti.getBody().setLayout(new FillLayout());
	}

	@Override
	protected void showTab(MUIElement element) {
		super.showTab(element);

		// an invisible element won't have the correct widget hierarchy
		if (!element.isVisible()) {
			return;
		}

		final PShelf ctf = (PShelf) getParentWidget(element);
		PShelfItem cti = findItemForPart(element, null);
		if (cti == null) {
			createTab(element.getParent(), element);
			cti = findItemForPart(element, element.getParent());
		}
		Control ctrl = (Control) element.getWidget();
		if (ctrl != null && ctrl.getParent() != ctf) {
			ctrl.setParent(ctf);
		} else if (element.getWidget() == null) {
			Control tabCtrl = (Control) renderer.createGui(element);
		}

		ctf.setSelection(cti);
	}

	@Override
	public void hookControllerLogic(final MUIElement me) {
		super.hookControllerLogic(me);

		if (!(me instanceof MElementContainer<?>))
			return;

		@SuppressWarnings("unchecked")
		final MElementContainer<MUIElement> stack = (MElementContainer<MUIElement>) me;

		// Match the selected TabItem to its Part
		final PShelf pShelf = (PShelf) me.getWidget();

		pShelf.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MUIElement ele = (MUIElement) e.item.getData(OWNING_ME);
				if (ele instanceof MPlaceholder) {
					ele.getParent().setSelectedElement(ele);
					ele = ((MPlaceholder) ele).getRef();
				}
				if (ele instanceof MPart) {
					ele.getParent().setSelectedElement(ele);
					activate((MPart) ele);
				}
			}
		});
	}

	private PShelfItem findItemForPart(MUIElement element, MElementContainer<MUIElement> stack) {
		if (stack == null)
			stack = element.getParent();
		if (!(stack.getWidget() instanceof CTabFolder))
			return null;
		PShelf ctf = (PShelf) stack.getWidget();
		if (ctf == null || ctf.isDisposed())
			return null;

		PShelfItem[] items = ctf.getItems();
		for (PShelfItem item : items) {
			if (item.getData(OWNING_ME) == element)
				return item;
		}
		return null;
	}

	private int calcIndexFor(MElementContainer<MUIElement> stack, final MUIElement part) {
		int index = 0;

		// Find the -visible- part before this element
		for (MUIElement mPart : stack.getChildren()) {
			if (mPart == part)
				return index;
			if (mPart.isToBeRendered() && mPart.isVisible())
				index++;
		}
		return index;
	}

	private String getLabel(MUILabel itemPart, String newName) {
		if (newName == null) {
			newName = ""; //$NON-NLS-1$
		} else {
			newName = LegacyActionTools.escapeMnemonics(newName);
		}

		if (itemPart instanceof MDirtyable && ((MDirtyable) itemPart).isDirty()) {
			newName = '*' + newName;
		}
		return newName;
	}

	private String getToolTip(String newToolTip) {
		return newToolTip == null || newToolTip.length() == 0 ? null : LegacyActionTools.escapeMnemonics(newToolTip);
	}
}
