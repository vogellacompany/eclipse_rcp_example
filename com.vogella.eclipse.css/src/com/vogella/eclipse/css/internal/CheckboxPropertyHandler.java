package com.vogella.eclipse.css.internal;

import org.eclipse.e4.ui.css.core.dom.properties.ICSSPropertyHandler;
import org.eclipse.e4.ui.css.core.dom.properties.converters.ICSSValueConverter;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.swt.properties.AbstractCSSPropertySWTHandler;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.w3c.dom.css.CSSValue;

import com.vogella.swt.widgets.Checkbox;

@SuppressWarnings("restriction")
public class CheckboxPropertyHandler extends AbstractCSSPropertySWTHandler implements ICSSPropertyHandler {

    private static final String CHECK_COLOR = "check-color";
    private static final String CENTER_COLOR = "center-color";

    @Override
    protected void applyCSSProperty(Control control, String property, CSSValue value, String pseudo, CSSEngine engine)
            throws Exception {
        if (control instanceof Checkbox) {
            Checkbox checkboxWidget = (Checkbox) control;
            if (CHECK_COLOR.equalsIgnoreCase(property) && (value.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE)) {
                Color newColor = (Color) engine.convert(value, Color.class, control.getDisplay());
                checkboxWidget.setCheckColor(newColor);
            } else if (CENTER_COLOR.equalsIgnoreCase(property)
                    && (value.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE)) {
                Color newColor = (Color) engine.convert(value, Color.class, control.getDisplay());
                checkboxWidget.setCenterColor(newColor);
            }
        }
    }

    @Override
    protected String retrieveCSSProperty(Control control, String property, String pseudo, CSSEngine engine)
            throws Exception {
        if (control instanceof Checkbox) {
            Checkbox checkboxWidget = (Checkbox) control;
            if (CHECK_COLOR.equalsIgnoreCase(property)) {
                ICSSValueConverter cssValueConverter = engine.getCSSValueConverter(String.class);
                return cssValueConverter.convert(checkboxWidget.getCheckColor(), engine, null);
            } else if (CENTER_COLOR.equalsIgnoreCase(property)) {
                ICSSValueConverter cssValueConverter = engine.getCSSValueConverter(String.class);
                return cssValueConverter.convert(checkboxWidget.getCenterColor(), engine, null);
            }
        }
        return null;
    }

}