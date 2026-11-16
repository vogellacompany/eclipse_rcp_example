package com.vogella.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * A round button which floats above a sibling control.
 */
public class FloatingButton extends Canvas {

	private static final int SIZE = 40;
	private static final int CIRCLE_INSET = 1;
	private static final int STEPS = 64;

	private Image image;
	private Control surface;
	private Runnable action = () -> {
	};

	public FloatingButton(Composite parent) {
		super(parent, SWT.DOUBLE_BUFFERED);
		setCursor(getDisplay().getSystemCursor(SWT.CURSOR_HAND));

		// a region is the only way to get a non rectangular control over a sibling
		Region circle = new Region();
		circle.add(circlePoints());
		setRegion(circle);
		addDisposeListener(_ -> circle.dispose());

		addPaintListener(e -> {
			// a region has hard edges, so blend a smaller circle into the color below
			e.gc.setBackground((surface != null ? surface : getParent()).getBackground());
			e.gc.fillRectangle(0, 0, SIZE, SIZE);
			e.gc.setAntialias(SWT.ON);
			e.gc.setBackground(getBackground());
			e.gc.fillOval(CIRCLE_INSET, CIRCLE_INSET, SIZE - 2 * CIRCLE_INSET, SIZE - 2 * CIRCLE_INSET);
			if (image != null) {
				Rectangle bounds = image.getBounds();
				e.gc.drawImage(image, (SIZE - bounds.width) / 2, (SIZE - bounds.height) / 2);
			}
		});
		addListener(SWT.MouseUp, e -> {
			if (e.button == 1 && getClientArea().contains(e.x, e.y)) {
				action.run();
			}
		});
	}

	public void setImage(Image image) {
		this.image = image;
		redraw();
	}

	/** Sets the control below this button, whose color the edge blends into. */
	public void setSurface(Control surface) {
		this.surface = surface;
		redraw();
	}

	public void setAction(Runnable action) {
		this.action = action;
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		return new Point(SIZE, SIZE);
	}

	private static int[] circlePoints() {
		double radius = SIZE / 2.0;
		int[] points = new int[STEPS * 2];
		for (int i = 0; i < STEPS; i++) {
			double angle = 2 * Math.PI * i / STEPS;
			points[i * 2] = (int) Math.round(radius + radius * Math.cos(angle));
			points[i * 2 + 1] = (int) Math.round(radius + radius * Math.sin(angle));
		}
		return points;
	}
}
