package com.vogella.swt.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

/**
 * A horizontal bar split into segments. The width of each segment is proportional to its count.
 * Clicking a segment, or pressing Enter or Space on it, selects it and fires a selection event
 * whose {@code data} is the segment id (or {@code null} if the selection was cleared).
 */
public class SegmentedBar extends Canvas {

	public record Segment(String id, String label, int count, RGB color) {
	}

	private static final int PADDING = 6;

	private List<Segment> segments = List.of();
	private final Map<RGB, Color> colorCache = new HashMap<>();
	private String selectedId;
	private int hoverIndex = -1;
	private int focusIndex = -1;
	private Color selectionColor;
	private Color hoverColor;

	public SegmentedBar(Composite parent, int style) {
		super(parent, style | SWT.DOUBLE_BUFFERED);
		addListener(SWT.Paint, this::paint);
		addListener(SWT.Resize, e -> redraw());
		addListener(SWT.MouseMove, e -> setHoverIndex(indexAt(e.x, e.y)));
		addListener(SWT.MouseExit, e -> setHoverIndex(-1));
		addListener(SWT.MouseDown, e -> {
			int index = indexAt(e.x, e.y);
			if (e.button == 1 && index >= 0) {
				setFocus();
				focusIndex = index;
				toggle(index);
			}
		});
		addListener(SWT.Traverse, this::traverse);
		addListener(SWT.KeyDown, this::keyDown);
		addListener(SWT.FocusIn, e -> {
			if (focusIndex < 0) {
				focusIndex = indexOfId(selectedId) >= 0 ? indexOfId(selectedId) : nextVisible(-1, 1);
			}
			redraw();
		});
		addListener(SWT.FocusOut, e -> redraw());
		addListener(SWT.Dispose, e -> colorCache.values().forEach(Color::dispose));

		getAccessible().addAccessibleListener(new AccessibleAdapter() {
			@Override
			public void getName(AccessibleEvent e) {
				e.result = describe();
			}
		});
	}

	// Painting

	private void paint(Event e) {
		GC gc = e.gc;
		gc.setAntialias(SWT.ON);
		gc.setBackground(getBackground());
		gc.fillRectangle(getClientArea());

		List<Rectangle> bounds = layoutSegments();
		for (int i = 0; i < segments.size(); i++) {
			Rectangle r = bounds.get(i);
			if (r.width == 0) {
				continue;
			}
			Segment segment = segments.get(i);
			Color fill = colorFor(segment.color());
			gc.setBackground(fill);
			gc.fillRectangle(r);

			if (i == hoverIndex) {
				gc.setAlpha(70);
				gc.setBackground(getHoverColor());
				gc.fillRectangle(r);
				gc.setAlpha(255);
			}

			gc.setForeground(isDark(segment.color()) ? getDisplay().getSystemColor(SWT.COLOR_WHITE)
					: getDisplay().getSystemColor(SWT.COLOR_BLACK));
			String text = String.valueOf(segment.count());
			Point extent = gc.textExtent(text);
			gc.drawText(text, r.x + (r.width - extent.x) / 2, r.y + (r.height - extent.y) / 2, true);

			if (segment.id().equals(selectedId)) {
				gc.setForeground(getSelectionColor());
				gc.setLineWidth(3);
				gc.drawRectangle(r.x + 1, r.y + 1, r.width - 3, r.height - 3);
				gc.setLineWidth(1);
			}
			if (i == focusIndex && isFocusControl()) {
				gc.drawFocus(r.x + 3, r.y + 3, r.width - 6, r.height - 6);
			}
		}
		if (bounds.stream().allMatch(r -> r.width == 0)) {
			gc.setForeground(getForeground());
			gc.drawText("No data", PADDING, PADDING, true);
		}
	}

	/** Splits the client area into one rectangle per segment, empty segments get a width of 0. */
	private List<Rectangle> layoutSegments() {
		Rectangle area = getClientArea();
		int total = segments.stream().mapToInt(Segment::count).sum();
		List<Rectangle> result = new ArrayList<>();
		int x = area.x;
		int counted = 0;
		for (Segment segment : segments) {
			counted += segment.count();
			// derive the right edge from the running sum so rounding errors do not add up
			int right = total == 0 ? x : area.x + (int) ((long) area.width * counted / total);
			result.add(new Rectangle(x, area.y, right - x, area.height));
			x = right;
		}
		return result;
	}

	private Color colorFor(RGB rgb) {
		return colorCache.computeIfAbsent(rgb, key -> new Color(getDisplay(), key));
	}

	private static boolean isDark(RGB rgb) {
		return 0.299 * rgb.red + 0.587 * rgb.green + 0.114 * rgb.blue < 140;
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();
		GC gc = new GC(this);
		int textHeight = gc.getFontMetrics().getHeight();
		gc.dispose();
		int width = wHint != SWT.DEFAULT ? wHint : 200;
		int height = hHint != SWT.DEFAULT ? hHint : textHeight + 2 * PADDING;
		return new Point(width, height);
	}

	// Hit testing, hover and tooltip

	/** Returns the segment at the given position or {@code null} if there is none. */
	public Segment getSegmentAt(int x, int y) {
		checkWidget();
		int index = indexAt(x, y);
		return index >= 0 ? segments.get(index) : null;
	}

	private int indexAt(int x, int y) {
		List<Rectangle> bounds = layoutSegments();
		for (int i = 0; i < bounds.size(); i++) {
			if (bounds.get(i).contains(x, y)) {
				return i;
			}
		}
		return -1;
	}

	private void setHoverIndex(int index) {
		if (index != hoverIndex) {
			hoverIndex = index;
			setToolTipText(index >= 0 ? tooltip(segments.get(index)) : null);
			redraw();
		}
	}

	private static String tooltip(Segment segment) {
		return segment.label() + ": " + segment.count();
	}

	// Keyboard

	private void traverse(Event e) {
		// keep arrow keys and Enter for the widget, Tab still moves the focus
		switch (e.detail) {
		case SWT.TRAVERSE_ARROW_NEXT, SWT.TRAVERSE_ARROW_PREVIOUS, SWT.TRAVERSE_RETURN -> e.doit = false;
		default -> {
		}
		}
	}

	private void keyDown(Event e) {
		switch (e.keyCode) {
		case SWT.ARROW_RIGHT -> moveFocus(1);
		case SWT.ARROW_LEFT -> moveFocus(-1);
		case SWT.CR, SWT.KEYPAD_CR, ' ' -> {
			if (focusIndex >= 0) {
				toggle(focusIndex);
			}
		}
		default -> {
		}
		}
	}

	private void moveFocus(int direction) {
		int next = nextVisible(focusIndex, direction);
		if (next >= 0) {
			focusIndex = next;
			redraw();
		}
	}

	/** Returns the next segment with a non-zero count in the given direction or -1. */
	private int nextVisible(int from, int direction) {
		for (int i = from + direction; i >= 0 && i < segments.size(); i += direction) {
			if (segments.get(i).count() > 0) {
				return i;
			}
		}
		return -1;
	}

	// Selection

	private void toggle(int index) {
		Segment segment = segments.get(index);
		boolean clear = segment.id().equals(selectedId);
		selectedId = clear ? null : segment.id();
		redraw();

		Event event = new Event();
		event.widget = this;
		event.data = selectedId;
		event.text = segment.label();
		notifyListeners(SWT.Selection, event);
	}

	/** Returns the id of the selected segment or {@code null}. */
	public String getSelectedId() {
		checkWidget();
		return selectedId;
	}

	public void setSelectedId(String id) {
		checkWidget();
		selectedId = id;
		redraw();
	}

	public void addSelectionListener(SelectionListener listener) {
		checkWidget();
		if (listener == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		addTypedListener(listener, SWT.Selection);
	}

	public void removeSelectionListener(SelectionListener listener) {
		checkWidget();
		if (listener == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		removeTypedListener(SWT.Selection, listener);
	}

	// Data

	/** Replaces the segments. A selection on a segment that is gone or empty is cleared. */
	public void setSegments(List<Segment> newSegments) {
		checkWidget();
		if (newSegments == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		segments = List.copyOf(newSegments);
		int selected = indexOfId(selectedId);
		if (selected < 0 || segments.get(selected).count() == 0) {
			selectedId = null;
		}
		hoverIndex = -1;
		focusIndex = nextVisible(-1, 1);
		redraw();
	}

	private int indexOfId(String id) {
		for (int i = 0; i < segments.size(); i++) {
			if (segments.get(i).id().equals(id)) {
				return i;
			}
		}
		return -1;
	}

	private String describe() {
		List<String> parts = new ArrayList<>();
		for (Segment segment : segments) {
			parts.add(tooltip(segment));
		}
		return String.join(", ", parts);
	}

	// Colors, setters can be called by a CSS property handler. The caller owns the colors.

	public Color getSelectionColor() {
		checkWidget();
		return selectionColor != null ? selectionColor : getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION);
	}

	public void setSelectionColor(Color color) {
		checkWidget();
		selectionColor = color;
		redraw();
	}

	public Color getHoverColor() {
		checkWidget();
		return hoverColor != null ? hoverColor : getDisplay().getSystemColor(SWT.COLOR_WHITE);
	}

	public void setHoverColor(Color color) {
		checkWidget();
		hoverColor = color;
		redraw();
	}
}
