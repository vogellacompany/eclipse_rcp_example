package com.vogella.tasks.ui.addon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class ThemeIconAddonTest {

    private static final String LIGHT = "platform:/plugin/com.vogella.tasks.ui/images/addTask.svg";
    private static final String DARK = "platform:/plugin/com.vogella.tasks.ui/images/dark/addTask.svg";

    @Test
    void usesDarkVariantInDarkTheme() {
        assertEquals(DARK, ThemeIconAddon.themedUri(LIGHT, true));
    }

    @Test
    void switchesBackToLightVariant() {
        assertEquals(LIGHT, ThemeIconAddon.themedUri(DARK, false));
    }

    @Test
    void keepsIconWithoutDarkVariant() {
        String png = "platform:/plugin/com.vogella.tasks.ui/images/vogella.png";
        assertEquals(png, ThemeIconAddon.themedUri(png, true));
    }

    @Test
    void ignoresMissingIcon() {
        assertNull(ThemeIconAddon.themedUri(null, true));
        assertEquals("", ThemeIconAddon.themedUri("", true));
    }
}
