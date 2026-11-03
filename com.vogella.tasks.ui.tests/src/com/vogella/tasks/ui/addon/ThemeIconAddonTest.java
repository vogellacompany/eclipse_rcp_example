package com.vogella.tasks.ui.addon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class ThemeIconAddonTest {

    private static final String ICON = "platform:/plugin/com.vogella.tasks.ui/img/addTask.svg";

    @Test
    void addsThemeAsFragment() {
        assertEquals(ICON + "#dark", ThemeIconAddon.themedUri(ICON, "dark"));
    }

    @Test
    void replacesFragmentOfPreviousTheme() {
        assertEquals(ICON + "#sepia", ThemeIconAddon.themedUri(ICON + "#dark", "sepia"));
    }

    @Test
    void ignoresMissingIcon() {
        assertNull(ThemeIconAddon.themedUri(null, "dark"));
        assertEquals("", ThemeIconAddon.themedUri("", "dark"));
    }
}
