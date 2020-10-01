package com.vogella.nattable.parts;

import java.util.Arrays;

import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.edit.editor.ComboBoxCellEditor;
import org.eclipse.nebula.widgets.nattable.edit.editor.ICellEditor;
import org.eclipse.nebula.widgets.nattable.filterrow.FilterRowDataLayer;
import org.eclipse.nebula.widgets.nattable.filterrow.FilterRowRegularExpressionConverter;
import org.eclipse.nebula.widgets.nattable.filterrow.FilterRowTextCellEditor;
import org.eclipse.nebula.widgets.nattable.filterrow.TextMatchingMode;
import org.eclipse.nebula.widgets.nattable.filterrow.config.FilterRowConfigAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;

import com.vogella.model.person.Person.Gender;

class FilterRowConfiguration extends AbstractRegistryConfiguration {

	@Override
	public void configureRegistry(IConfigRegistry configRegistry) {

		// register the FilterRowTextCellEditor in the first column which
		// immediately commits on key press
		configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new FilterRowTextCellEditor(),
				DisplayMode.NORMAL, FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 0);

		configRegistry.registerConfigAttribute(FilterRowConfigAttributes.TEXT_MATCHING_MODE,
				TextMatchingMode.REGULAR_EXPRESSION, DisplayMode.NORMAL,
				FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 0);

		configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER,
				new FilterRowRegularExpressionConverter(), DisplayMode.NORMAL,
				FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 0);

		// register a combo box cell editor for the gender column in the
		// filter row the label is set automatically to the value of
		// FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + column
		// position
		ICellEditor comboBoxCellEditor = new ComboBoxCellEditor(Arrays.asList(Gender.FEMALE, Gender.MALE));
		configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, comboBoxCellEditor, DisplayMode.NORMAL,
				FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 2);

		configRegistry.registerConfigAttribute(FilterRowConfigAttributes.TEXT_MATCHING_MODE, TextMatchingMode.EXACT,
				DisplayMode.NORMAL, FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 2);

		// register a combo box cell editor for the married column in the
		// filter row the label is set automatically to the value of
		// FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + column
		// position
		comboBoxCellEditor = new ComboBoxCellEditor(Arrays.asList(Boolean.TRUE, Boolean.FALSE));
		configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, comboBoxCellEditor, DisplayMode.NORMAL,
				FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 3);

		configRegistry.registerConfigAttribute(FilterRowConfigAttributes.TEXT_DELIMITER, "&"); //$NON-NLS-1$

	}
}
