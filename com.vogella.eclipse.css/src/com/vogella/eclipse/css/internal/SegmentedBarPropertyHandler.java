package com.vogella.eclipse.css.internal;

import org.eclipse.e4.ui.css.core.dom.properties.converters.ICSSValueConverter;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.swt.properties.AbstractCSSPropertySWTHandler;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.w3c.dom.css.CSSValue;

import com.vogella.swt.widgets.SegmentedBar;

/** Supports the {@code selection-color}, {@code hover-color} and {@code separator-color} CSS properties for {@link SegmentedBar}. */
@SuppressWarnings("restriction")
public class SegmentedBarPropertyHandler extends AbstractCSSPropertySWTHandler {

    private static final String SELECTION_COLOR = "selection-color";
    private static final String HOVER_COLOR = "hover-color";
    private static final String SEPARATOR_COLOR = "separator-color";

    @Override
    protected void applyCSSProperty(Control control, String property, CSSValue value, String pseudo, CSSEngine engine)
            throws Exception {
        if (control instanceof SegmentedBar bar && value.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE) {
            Color color = (Color) engine.convert(value, Color.class, control.getDisplay());
            if (SELECTION_COLOR.equalsIgnoreCase(property)) {
                bar.setSelectionColor(color);
            } else if (HOVER_COLOR.equalsIgnoreCase(property)) {
                bar.setHoverColor(color);
            } else if (SEPARATOR_COLOR.equalsIgnoreCase(property)) {
                bar.setSeparatorColor(color);
            }
        }
    }

    @Override
    protected String retrieveCSSProperty(Control control, String property, String pseudo, CSSEngine engine)
            throws Exception {
        if (control instanceof SegmentedBar bar) {
            ICSSValueConverter converter = engine.getCSSValueConverter(String.class);
            if (SELECTION_COLOR.equalsIgnoreCase(property)) {
                return converter.convert(bar.getSelectionColor(), engine, null);
            } else if (HOVER_COLOR.equalsIgnoreCase(property)) {
                return converter.convert(bar.getHoverColor(), engine, null);
            } else if (SEPARATOR_COLOR.equalsIgnoreCase(property)) {
                return converter.convert(bar.getSeparatorColor(), engine, null);
            }
        }
        return null;
    }
}
