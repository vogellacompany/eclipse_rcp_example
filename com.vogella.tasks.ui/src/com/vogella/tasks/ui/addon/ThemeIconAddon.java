package com.vogella.tasks.ui.addon;

import java.net.URI;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.css.swt.theme.ITheme;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.descriptor.basic.MPartDescriptor;
import org.eclipse.e4.ui.model.application.ui.MUILabel;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.osgi.service.event.Event;

import jakarta.inject.Inject;

/**
 * Switches the icons of the application model to the variant in a {@code dark} sub folder of the
 * icon's folder while a dark theme is active.
 */
public class ThemeIconAddon {

    private static final String DARK_FOLDER = "/dark";

    @Inject
    private MApplication application;

    @Inject
    private EModelService modelService;

    @Inject
    @Optional
    public void themeChanged(@UIEventTopic(IThemeEngine.Events.THEME_CHANGED) Event event) {
        ITheme theme = (ITheme) event.getProperty(IThemeEngine.Events.THEME);
        // same convention the theme engine uses for Display.setDarkThemePreferred
        boolean dark = theme != null && theme.getId().contains("dark");

        int searchFlags = EModelService.ANYWHERE | EModelService.IN_PART | EModelService.IN_MAIN_MENU;
        for (MUILabel label : modelService.findElements(application, MUILabel.class, searchFlags, e -> true)) {
            String uri = themedUri(label.getIconURI(), dark);
            if (uri != null && !uri.equals(label.getIconURI())) {
                label.setIconURI(uri);
            }
        }
        // parts created later copy the icon from their descriptor
        for (MPartDescriptor descriptor : application.getDescriptors()) {
            String uri = themedUri(descriptor.getIconURI(), dark);
            if (uri != null && !uri.equals(descriptor.getIconURI())) {
                descriptor.setIconURI(uri);
            }
        }
        for (MCommand command : application.getCommands()) {
            String uri = themedUri(command.getCommandIconURI(), dark);
            if (uri != null && !uri.equals(command.getCommandIconURI())) {
                command.setCommandIconURI(uri);
            }
        }
    }

    /** Returns the URI for the given theme, the light URI if the icon has no dark variant. */
    static String themedUri(String uri, boolean dark) {
        if (uri == null || uri.indexOf('/') < 0) {
            return uri;
        }
        String light = lightVariant(uri);
        if (!dark) {
            return light;
        }
        String darkUri = darkVariant(light);
        return exists(darkUri) ? darkUri : light;
    }

    static String lightVariant(String uri) {
        int slash = uri.lastIndexOf('/');
        String folder = uri.substring(0, slash);
        return folder.endsWith(DARK_FOLDER)
                ? folder.substring(0, folder.length() - DARK_FOLDER.length()) + uri.substring(slash)
                : uri;
    }

    static String darkVariant(String uri) {
        int slash = uri.lastIndexOf('/');
        return uri.substring(0, slash) + DARK_FOLDER + uri.substring(slash);
    }

    private static boolean exists(String uri) {
        try {
            return FileLocator.find(URI.create(uri).toURL()) != null;
        } catch (Exception e) {
            return false;
        }
    }
}
