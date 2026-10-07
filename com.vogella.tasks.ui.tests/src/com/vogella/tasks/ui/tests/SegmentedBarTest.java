package com.vogella.tasks.ui.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.vogella.swt.widgets.SegmentedBar;
import com.vogella.swt.widgets.SegmentedBar.Segment;

public class SegmentedBarTest {

	private Shell shell;
	private SegmentedBar bar;
	private final List<Object> selections = new ArrayList<>();

	@BeforeEach
	public void setUp() {
		shell = new Shell(Display.getDefault());
		bar = new SegmentedBar(shell, SWT.NONE);
		bar.setSize(400, 30);
		// 1 : 3 : 0 : 4 results in segments of 50, 150, 0 and 200 pixels
		bar.setSegments(List.of(segment("a", 1), segment("b", 3), segment("c", 0), segment("d", 4)));
		bar.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> selections.add(e.data)));
	}

	@AfterEach
	public void tearDown() {
		shell.dispose();
	}

	@Test
	public void hitTestingIsProportionalToTheCounts() {
		assertEquals("a", bar.getSegmentAt(10, 10).id());
		assertEquals("b", bar.getSegmentAt(60, 10).id());
		assertEquals("d", bar.getSegmentAt(210, 10).id());
		assertEquals("d", bar.getSegmentAt(399, 10).id());
		assertNull(bar.getSegmentAt(400, 10));
	}

	@Test
	public void clickSelectsAndSecondClickClearsTheSelection() {
		click(60, 10);
		assertEquals("b", bar.getSelectedId());
		click(60, 10);
		assertNull(bar.getSelectedId());
		assertEquals(Arrays.asList("b", null), selections);
	}

	@Test
	public void keyboardSkipsEmptySegmentsAndSelectsWithSpace() {
		key(SWT.ARROW_RIGHT);
		key(SWT.ARROW_RIGHT);
		key(SWT.ARROW_RIGHT);
		key(' ');
		assertEquals("d", bar.getSelectedId());
		assertEquals(List.of("d"), selections);
	}

	@Test
	public void selectionIsClearedWhenItsSegmentBecomesEmpty() {
		bar.setSelectedId("b");
		bar.setSegments(List.of(segment("a", 1), segment("b", 0)));
		assertNull(bar.getSelectedId());
	}

	private static Segment segment(String id, int count) {
		return new Segment(id, id, count, new RGB(200, 100, 50));
	}

	private void click(int x, int y) {
		Event event = new Event();
		event.x = x;
		event.y = y;
		event.button = 1;
		bar.notifyListeners(SWT.MouseDown, event);
	}

	private void key(int keyCode) {
		Event event = new Event();
		event.keyCode = keyCode;
		bar.notifyListeners(SWT.KeyDown, event);
	}
}
