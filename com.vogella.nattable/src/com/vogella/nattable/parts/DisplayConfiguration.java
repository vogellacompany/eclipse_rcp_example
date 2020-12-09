package com.vogella.nattable.parts;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultDateDisplayConverter;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.edit.editor.CheckBoxCellEditor;
import org.eclipse.nebula.widgets.nattable.edit.editor.DateCellEditor;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.painter.cell.CheckBoxPainter;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.HorizontalAlignmentEnum;
import org.eclipse.nebula.widgets.nattable.style.Style;

public class DisplayConfiguration extends AbstractRegistryConfiguration {

    @Override
    public void configureRegistry(IConfigRegistry configRegistry) {
		configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE, IEditableRule.NEVER_EDITABLE);

        registerConfiguration(configRegistry);
    }

    private void registerConfiguration(IConfigRegistry configRegistry) {
		configureIdColumn(configRegistry, 0);
        configureTextColumn(configRegistry, 1);
		configureBooleanColumn(configRegistry, 2);
		configureBooleanColumn(configRegistry, 3);
		configureDateColumn(configRegistry, 4);
    }


	private void configureDateColumn(IConfigRegistry configRegistry, int i) {
		// TODO Auto-generated method stub

	}

	private void configureIdColumn(IConfigRegistry configRegistry, int columnIndex) {
		Style cellStyle = new Style();
		cellStyle.setAttributeValue(CellStyleAttributes.HORIZONTAL_ALIGNMENT, HorizontalAlignmentEnum.RIGHT);
		configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL,
				ColumnLabelAccumulator.COLUMN_LABEL_PREFIX + columnIndex);
	}
    private void configureTextColumn(IConfigRegistry configRegistry, int columnIndex) {
        // register a TextCellEditor for column two that commits on key up/down
        // moves the selection after commit by enter

		Style cellStyle = new Style();
		cellStyle.setAttributeValue(CellStyleAttributes.HORIZONTAL_ALIGNMENT, HorizontalAlignmentEnum.LEFT);
		configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL,
				ColumnLabelAccumulator.COLUMN_LABEL_PREFIX + columnIndex);

    }

	private void configureBooleanColumn(IConfigRegistry configRegistry, int columnIndex) {

		configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER,
				new CheckBoxPainter(),
                DisplayMode.NORMAL, ColumnLabelAccumulator.COLUMN_LABEL_PREFIX + columnIndex);
		// using a CheckBoxCellEditor also needs a Boolean conversion to work
		// correctly
		configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER,
				new RevertBooleanDisplayConverter(), DisplayMode.NORMAL,
				ColumnLabelAccumulator.COLUMN_LABEL_PREFIX + columnIndex);
    }

    private void registerMarriedEditor(IConfigRegistry configRegistry, int columnIndex) {
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new CheckBoxCellEditor(),
                DisplayMode.EDIT, ColumnLabelAccumulator.COLUMN_LABEL_PREFIX + columnIndex);

        // The CheckBoxCellEditor can also be visualized like a check button
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, new CheckBoxPainter(),
                DisplayMode.NORMAL, ColumnLabelAccumulator.COLUMN_LABEL_PREFIX + columnIndex);


    }

    private void registerBirthdayEditor(IConfigRegistry configRegistry, int columnIndex) {
        DateCellEditor dateCellEditor = new DateCellEditor();
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, dateCellEditor, DisplayMode.EDIT,
                ColumnLabelAccumulator.COLUMN_LABEL_PREFIX + columnIndex);

        DateFormat formatter = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        String pattern = ((SimpleDateFormat) formatter).toPattern();

        // using a DateCellEditor also needs a Date conversion to work correctly
        configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER,
                new DefaultDateDisplayConverter(pattern), DisplayMode.NORMAL,
                ColumnLabelAccumulator.COLUMN_LABEL_PREFIX + columnIndex);
    }

}