package com.vogella.nattable.parts;

import org.eclipse.nebula.widgets.nattable.data.convert.DisplayConverter;

/**
 * Data type converter for a Check Box. Assumes that the data value is stored as
 * a boolean.
 */
public class RevertBooleanDisplayConverter extends DisplayConverter {

    @Override
    public Object displayToCanonicalValue(Object displayValue) {
		return !Boolean.valueOf(displayValue.toString());
    }

    @Override
    public Object canonicalToDisplayValue(Object canonicalValue) {
		System.out.println(canonicalValue);
        if (canonicalValue == null) {
            return null;
        }
		if (canonicalValue instanceof Boolean) {
			return !(Boolean) canonicalValue;
		}
		if (canonicalValue instanceof String) {
			String test = (String) canonicalValue;
			return ((test.startsWith("F") || test.startsWith("W")));
		}
		return true;
    }

}
