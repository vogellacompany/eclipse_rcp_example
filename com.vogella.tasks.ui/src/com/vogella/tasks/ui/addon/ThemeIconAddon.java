package com.vogella.tasks.ui.addon;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.css.swt.theme.ITheme;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.descriptor.basic.MPartDescriptor;
import org.eclipse.e4.ui.model.application.ui.MUILabel;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.internal.image.SVGFileFormat;
import org.osgi.service.event.Event;

import jakarta.inject.Inject;

/**
 * Applies the icon color and icon set of the active theme and reloads the icons of the application model.
 */
@SuppressWarnings("restriction")
public class ThemeIconAddon {

    private static final RGB LIGHT_ICONS = new RGB(0x42, 0x42, 0x42);
    private static final RGB DARK_ICONS = new RGB(0xC5, 0xC5, 0xC5);
    private static final String SEPIA = "sepia";

    @Inject
    private MApplication application;

    @Inject
    private EModelService modelService;

    @Inject
    @Optional
    public void themeChanged(@UIEventTopic(IThemeEngine.Events.THEME_CHANGED) Event event) {
        ITheme theme = (ITheme) event.getProperty(IThemeEngine.Events.THEME);
        String themeId = theme == null ? "" : theme.getId();

        SVGFileFormat.setCurrentColor(theme != null && theme.isDark() ? DARK_ICONS : LIGHT_ICONS);
        IconSetModifier.setIconSet(themeId.contains(SEPIA) ? SEPIA : null);

        int searchFlags = EModelService.ANYWHERE | EModelService.IN_PART | EModelService.IN_MAIN_MENU
                | EModelService.IN_SHARED_ELEMENTS;
        for (MUILabel label : modelService.findElements(application, MUILabel.class, searchFlags, _ -> true)) {
            label.setIconURI(themedUri(label.getIconURI(), themeId));
        }
        // parts created later copy the icon from their descriptor
        for (MPartDescriptor descriptor : application.getDescriptors()) {
            descriptor.setIconURI(themedUri(descriptor.getIconURI(), themeId));
        }
        for (MCommand command : application.getCommands()) {
            command.setCommandIconURI(themedUri(command.getCommandIconURI(), themeId));
        }
    }

    /**
     * Returns the URI with the theme as fragment. JFace caches images by URL, so an unchanged URL
     * would keep the color and icon set of the previous theme.
     */
    public static String themedUri(String uri, String themeId) {
        if (uri == null || uri.isEmpty()) {
            return uri;
        }
        int fragment = uri.indexOf('#');
        return (fragment < 0 ? uri : uri.substring(0, fragment)) + "#" + themeId;
    }
}
