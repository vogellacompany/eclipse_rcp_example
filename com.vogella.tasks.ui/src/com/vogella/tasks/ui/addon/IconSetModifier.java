package com.vogella.tasks.ui.addon;

import java.net.URL;

import org.eclipse.jface.internal.provisional.resource.IImageURLModifier;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * Loads the icons of this bundle from the sub folder of the active icon set.
 */
@SuppressWarnings("restriction")
@Component
public class IconSetModifier implements IImageURLModifier {

    private static final String ICON_FOLDER = "/com.vogella.tasks.ui/img/";

    private static volatile String iconSet;

    private Bundle bundle;

    @Activate
    void activate(BundleContext context) {
        bundle = context.getBundle();
    }

    /** Selects the sub folder of img to load icons from, {@code null} for img itself. */
    static void setIconSet(String name) {
        iconSet = name;
    }

    @Override
    public URL modifyURL(URL url) {
        String entry = entryPath(url.getPath(), iconSet);
        // an icon missing from the set keeps the original
        return entry == null ? null : bundle.getEntry(entry);
    }

    static String entryPath(String path, String set) {
        int start = path.indexOf(ICON_FOLDER);
        if (set == null || start < 0) {
            return null;
        }
        return "img/" + set + "/" + path.substring(start + ICON_FOLDER.length());
    }
}
