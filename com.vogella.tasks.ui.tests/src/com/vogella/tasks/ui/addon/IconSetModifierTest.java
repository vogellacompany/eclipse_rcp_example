package com.vogella.tasks.ui.addon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class IconSetModifierTest {

    private static final String ICON = "/plugin/com.vogella.tasks.ui/img/addTask.svg";

    @Test
    void mapsIconToActiveSet() {
        assertEquals("img/sepia/addTask.svg", IconSetModifier.entryPath(ICON, "sepia"));
    }

    @Test
    void keepsIconWithoutSet() {
        assertNull(IconSetModifier.entryPath(ICON, null));
    }

    @Test
    void ignoresIconsOfOtherBundles() {
        assertNull(IconSetModifier.entryPath("/plugin/org.eclipse.ui/icons/full/delete.svg", "sepia"));
    }
}
