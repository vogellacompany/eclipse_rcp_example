package com.vogella.nattable.parts;

import static org.eclipse.jface.widgets.WidgetFactory.button;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.ConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultBooleanDisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultDisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultDoubleDisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultIntegerDisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.convert.DisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.convert.IDisplayConverter;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.edit.editor.CheckBoxCellEditor;
import org.eclipse.nebula.widgets.nattable.edit.editor.ComboBoxCellEditor;
import org.eclipse.nebula.widgets.nattable.edit.editor.MultiLineTextCellEditor;
import org.eclipse.nebula.widgets.nattable.edit.editor.PasswordCellEditor;
import org.eclipse.nebula.widgets.nattable.edit.editor.TextCellEditor;
import org.eclipse.nebula.widgets.nattable.edit.gui.FileDialogCellEditor;
import org.eclipse.nebula.widgets.nattable.edit.gui.ICellEditDialog;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.GlazedListsEventLayer;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.groupBy.GroupByDataLayer;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.groupBy.GroupByHeaderLayer;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.groupBy.GroupByHeaderMenuConfiguration;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.groupBy.GroupByModel;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultRowHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.CornerLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultColumnHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultRowHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.RowHeaderLayer;
import org.eclipse.nebula.widgets.nattable.layer.AbstractLayerTransform;
import org.eclipse.nebula.widgets.nattable.layer.CompositeLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.painter.cell.CheckBoxPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.ComboBoxPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.PasswordTextPainter;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.sort.config.SingleClickSortConfiguration;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.HorizontalAlignmentEnum;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.nebula.widgets.nattable.tickupdate.TickUpdateConfigAttributes;
import org.eclipse.nebula.widgets.nattable.tree.TreeLayer;
import org.eclipse.nebula.widgets.nattable.tree.command.TreeCollapseAllCommand;
import org.eclipse.nebula.widgets.nattable.tree.command.TreeExpandAllCommand;
import org.eclipse.nebula.widgets.nattable.tree.command.TreeExpandToLevelCommand;
import org.eclipse.nebula.widgets.nattable.ui.menu.HeaderMenuConfiguration;
import org.eclipse.nebula.widgets.nattable.ui.menu.IMenuItemProvider;
import org.eclipse.nebula.widgets.nattable.ui.menu.PopupMenuBuilder;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.vogella.tasks.model.Task;
import com.vogella.tasks.model.TaskService;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.TransformedList;

public class SortHeaderPart {


	public static final String COLUMN_ONE_LABEL = "ColumnOneLabel";
	public static final String COLUMN_TWO_LABEL = "ColumnTwoLabel";
	public static final String COLUMN_THREE_LABEL = "ColumnThreeLabel";
	public static final String COLUMN_FOUR_LABEL = "ColumnFourLabel";
	public static final String COLUMN_FIVE_LABEL = "ColumnFiveLabel";
	public static final String COLUMN_SIX_LABEL = "ColumnSixLabel";
	public static final String COLUMN_SEVEN_LABEL = "ColumnSevenLabel";
	public static final String COLUMN_EIGHT_LABEL = "ColumnEightLabel";
	public static final String COLUMN_NINE_LABEL = "ColumnNineLabel";
	public static final String COLUMN_TEN_LABEL = "ColumnTenLabel";
	public static final String COLUMN_ELEVEN_LABEL = "ColumnElevenLabel";
	public static final String COLUMN_TWELVE_LABEL = "ColumnTwelveLabel";
	public static final String COLUMN_THIRTEEN_LABEL = "ColumnThirteenLabel";
	
	@Inject
	ESelectionService selectionService;

	private static final String[] propertyNames = { "summary", "description", "dueDate", "done" };

	private static final Map<String, String> propertyToLabelMap;

	private BodyLayerStack<Task> bodyLayerStack;

	private NatTable natTable;

	static {
		propertyToLabelMap = new HashMap<>();
		propertyToLabelMap.put("summary", "Summary");
		propertyToLabelMap.put("description", "Description");
		propertyToLabelMap.put("dueDate", "Due Date");
		propertyToLabelMap.put("done", "Done");
	}

	@PostConstruct
	public void postConstruct(Composite parent, TaskService taskService) {

		parent.setLayout(new GridLayout(1, false));
		button(SWT.PUSH).onSelect(e -> bodyLayerStack.getList().remove(0)).text("Delete").create(parent);
		button(SWT.PUSH).onSelect(e -> natTable.doCommand(new TreeCollapseAllCommand())).text("Collapse")
				.create(parent);
		button(SWT.PUSH).onSelect(e -> natTable.doCommand(new TreeExpandAllCommand())).text("Expand").create(parent);
		ConfigRegistry configRegistry = new ConfigRegistry();

//		// Data setup
//		EventList<Task> tasks = GlazedLists.eventList(taskService.getAll());
//		SortedList<Task> sortedList = new SortedList<>(tasks, null);

		IColumnPropertyAccessor<Task> accessor = new TaskColumnPropertyAccessor();
//		IDataProvider bodyDataProvider = new ListDataProvider<>(sortedList, accessor);

		bodyLayerStack = new BodyLayerStack<>(taskService.getAll(), accessor);

		// build the column header layer
		IDataProvider columnHeaderDataProvider = new DefaultColumnHeaderDataProvider(propertyNames, propertyToLabelMap);
		DataLayer columnHeaderDataLayer = new DefaultColumnHeaderDataLayer(columnHeaderDataProvider);
		ColumnHeaderLayer columnHeaderLayer = new ColumnHeaderLayer(columnHeaderDataLayer, bodyLayerStack,
				bodyLayerStack.getSelectionLayer());

		// build the row header layer
		IDataProvider rowHeaderDataProvider = new DefaultRowHeaderDataProvider(bodyLayerStack.getBodyDataProvider());
		DataLayer rowHeaderDataLayer = new DefaultRowHeaderDataLayer(rowHeaderDataProvider);
		ILayer rowHeaderLayer = new RowHeaderLayer(rowHeaderDataLayer, bodyLayerStack,
				bodyLayerStack.getSelectionLayer());

		// build the corner layer
		IDataProvider cornerDataProvider = new DefaultCornerDataProvider(columnHeaderDataProvider,
				rowHeaderDataProvider);
		DataLayer cornerDataLayer = new DataLayer(cornerDataProvider);
		ILayer cornerLayer = new CornerLayer(cornerDataLayer, rowHeaderLayer, columnHeaderLayer);

		// build the grid layer
		GridLayer gridLayer = new GridLayer(bodyLayerStack, columnHeaderLayer, rowHeaderLayer, cornerLayer);

		// set the group by header on top of the grid
		CompositeLayer compositeGridLayer = new CompositeLayer(1, 2);
		final GroupByHeaderLayer groupByHeaderLayer = new GroupByHeaderLayer(bodyLayerStack.getGroupByModel(),
				gridLayer, columnHeaderDataProvider, columnHeaderLayer);
		compositeGridLayer.setChildLayer(GroupByHeaderLayer.GROUP_BY_REGION, groupByHeaderLayer, 0, 0);
		compositeGridLayer.setChildLayer("Grid", gridLayer, 0, 1);

		natTable = new NatTable(parent, compositeGridLayer, false);

		// as the autoconfiguration of the NatTable is turned off, we have to
		// add the DefaultNatTableStyleConfiguration and the ConfigRegistry
		// manually
		natTable.setConfigRegistry(configRegistry);
		natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
		natTable.addConfiguration(new SingleClickSortConfiguration());
//        natTable.registerCommandHandler(new DisplayPersistenceDialogCommandHandler(natTable));

		// add group by configuration
		natTable.addConfiguration(new GroupByHeaderMenuConfiguration(natTable, groupByHeaderLayer));
		natTable.addConfiguration(new HeaderMenuConfiguration(natTable) {
			@Override
			protected PopupMenuBuilder createCornerMenu(NatTable natTable) {
				return super.createCornerMenu(natTable).withStateManagerMenuItemProvider()
						.withMenuItemProvider(new IMenuItemProvider() {

							@Override
							public void addMenuItem(NatTable natTable, Menu popupMenu) {
								MenuItem menuItem = new MenuItem(popupMenu, SWT.PUSH);
								menuItem.setText("Toggle Group By Header"); //$NON-NLS-1$
								menuItem.setEnabled(true);

								menuItem.addSelectionListener(new SelectionAdapter() {
									@Override
									public void widgetSelected(SelectionEvent event) {
										groupByHeaderLayer.setVisible(!groupByHeaderLayer.isVisible());
									}
								});
							}
						}).withMenuItemProvider(new IMenuItemProvider() {

							@Override
							public void addMenuItem(final NatTable natTable, Menu popupMenu) {
								MenuItem menuItem = new MenuItem(popupMenu, SWT.PUSH);
								menuItem.setText("Collapse All"); //$NON-NLS-1$
								menuItem.setEnabled(true);

								menuItem.addSelectionListener(new SelectionAdapter() {
									@Override
									public void widgetSelected(SelectionEvent event) {
										natTable.doCommand(new TreeCollapseAllCommand());
									}
								});
							}
						}).withMenuItemProvider(new IMenuItemProvider() {

							@Override
							public void addMenuItem(final NatTable natTable, Menu popupMenu) {
								MenuItem menuItem = new MenuItem(popupMenu, SWT.PUSH);
								menuItem.setText("Expand All"); //$NON-NLS-1$
								menuItem.setEnabled(true);

								menuItem.addSelectionListener(new SelectionAdapter() {
									@Override
									public void widgetSelected(SelectionEvent event) {
										natTable.doCommand(new TreeExpandAllCommand());
									}
								});
							}
						}).withMenuItemProvider(new IMenuItemProvider() {

							@Override
							public void addMenuItem(final NatTable natTable, Menu popupMenu) {
								MenuItem menuItem = new MenuItem(popupMenu, SWT.PUSH);
								menuItem.setText("Expand to Level 2"); //$NON-NLS-1$
								menuItem.setEnabled(true);

								menuItem.addSelectionListener(new SelectionAdapter() {
									@Override
									public void widgetSelected(SelectionEvent event) {
										natTable.doCommand(new TreeExpandToLevelCommand(2));
									}
								});
							}
						});
			}
		});

//		natTable.addConfiguration(new AbstractRegistryConfiguration() {
//			@Override
//			public void configureRegistry(IConfigRegistry configRegistry) {
//				configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE,
//						IEditableRule.ALWAYS_EDITABLE);
//			}
//		});
		
	    natTable.addConfiguration(new EditorConfiguration());
	     
		natTable.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
//		

//		DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);
//
//		SelectionLayer selectionLayer = new SelectionLayer(bodyDataLayer);
//		ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);
//
//		IDataProvider columnHeaderDataProvider = new DefaultColumnHeaderDataProvider(
//				propertyNames,propertyToLabelMap);
//		DataLayer columnHeaderDataLayer = new DefaultColumnHeaderDataLayer(columnHeaderDataProvider);
//		ILayer columnHeaderLayer = new ColumnHeaderLayer(columnHeaderDataLayer, viewportLayer, selectionLayer);
//
//		SortHeaderLayer<Person> sortHeaderLayer = new SortHeaderLayer<>(columnHeaderLayer,
//				new GlazedListsSortModel<>(sortedList, accessor, configRegistry, columnHeaderDataLayer));
//
//		CompositeLayer compositeLayer = new CompositeLayer(1, 2);
//		compositeLayer.setChildLayer(GridRegion.COLUMN_HEADER, sortHeaderLayer, 0, 0);
//		compositeLayer.setChildLayer(GridRegion.BODY, viewportLayer, 0, 1);

//		NatTable natTable = new NatTable(parent, compositeLayer, false);
//
//		natTable.setConfigRegistry(configRegistry);
//		natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
//		natTable.addConfiguration(new SingleClickSortConfiguration());

		natTable.configure();
	}

	/**
	 * Always encapsulate the body layer stack in an AbstractLayerTransform to
	 * ensure that the index transformations are performed in later commands.
	 *
	 * @param <T>
	 */
	class BodyLayerStack<T> extends AbstractLayerTransform {

		private final SortedList<T> sortedList;

		private final IDataProvider bodyDataProvider;

		private final SelectionLayer selectionLayer;

		private final GroupByModel groupByModel = new GroupByModel();

		private EventList<T> eventList;

		public BodyLayerStack(List<T> values, IColumnPropertyAccessor<T> columnPropertyAccessor) {
			eventList = GlazedLists.eventList(values);
			TransformedList<T, T> rowObjectsGlazedList = GlazedLists.threadSafeList(eventList);

			// use the SortedList constructor with 'null' for the Comparator
			// because the Comparator
			// will be set by configuration
			this.sortedList = new SortedList<>(rowObjectsGlazedList, null);

			// Use the GroupByDataLayer instead of the default DataLayer
			GroupByDataLayer<T> bodyDataLayer = new GroupByDataLayer<>(getGroupByModel(), this.sortedList,
					columnPropertyAccessor);
			
			  final ColumnOverrideLabelAccumulator columnLabelAccumulator =
		                new ColumnOverrideLabelAccumulator(bodyDataLayer);
		        bodyDataLayer.setConfigLabelAccumulator(columnLabelAccumulator);
		        registerColumnLabels(columnLabelAccumulator);
		        
			// get the IDataProvider that was created by the GroupByDataLayer
			this.bodyDataProvider = bodyDataLayer.getDataProvider();
			// layer for event handling of GlazedLists and PropertyChanges
			GlazedListsEventLayer<T> glazedListsEventLayer = new GlazedListsEventLayer<>(bodyDataLayer,
					this.sortedList);

			this.selectionLayer = new SelectionLayer(glazedListsEventLayer);

			// add a tree layer to visualise the grouping
			TreeLayer treeLayer = new TreeLayer(this.selectionLayer, bodyDataLayer.getTreeRowModel());

			ViewportLayer viewportLayer = new ViewportLayer(treeLayer);

			setUnderlyingLayer(viewportLayer);
		}

		public SelectionLayer getSelectionLayer() {
			return this.selectionLayer;
		}

		public SortedList<T> getSortedList() {
			return this.sortedList;
		}

		public IDataProvider getBodyDataProvider() {
			return this.bodyDataProvider;
		}

		public GroupByModel getGroupByModel() {
			return this.groupByModel;
		}

		public EventList<T> getList() {
			return eventList;
		}
	}
	

    private void registerColumnLabels(
            ColumnOverrideLabelAccumulator columnLabelAccumulator) {
//        columnLabelAccumulator.registerColumnOverrides(0, COLUMN_ONE_LABEL);
//        columnLabelAccumulator.registerColumnOverrides(1, COLUMN_TWO_LABEL);
//        columnLabelAccumulator.registerColumnOverrides(2, COLUMN_THREE_LABEL);
//        columnLabelAccumulator.registerColumnOverrides(3, COLUMN_FOUR_LABEL);
//        columnLabelAccumulator.registerColumnOverrides(4, COLUMN_FIVE_LABEL);
//        columnLabelAccumulator.registerColumnOverrides(5, COLUMN_SIX_LABEL);
//        columnLabelAccumulator.registerColumnOverrides(6, COLUMN_SEVEN_LABEL);
//        columnLabelAccumulator.registerColumnOverrides(7, COLUMN_EIGHT_LABEL);
//        columnLabelAccumulator.registerColumnOverrides(8, COLUMN_NINE_LABEL);
//        columnLabelAccumulator.registerColumnOverrides(9, COLUMN_TEN_LABEL);
//        columnLabelAccumulator.registerColumnOverrides(10, COLUMN_ELEVEN_LABEL);
//        columnLabelAccumulator.registerColumnOverrides(11, COLUMN_TWELVE_LABEL);
//        columnLabelAccumulator.registerColumnOverrides(12, COLUMN_THIRTEEN_LABEL);
    }

	class EditorConfiguration extends AbstractRegistryConfiguration {

		@Override
		public void configureRegistry(IConfigRegistry configRegistry) {
			configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE,
					IEditableRule.ALWAYS_EDITABLE);

			registerEditors(configRegistry);
		}

		private void registerEditors(IConfigRegistry configRegistry) {
			registerColumnTwoTextEditor(configRegistry);
			registerColumnThreePasswordEditor(configRegistry);
			registerColumnFourMultiLineEditor(configRegistry);
			registerColumnFiveIntegerEditor(configRegistry);
			registerColumnSixDoubleEditor(configRegistry);
			registerColumnSevenCheckbox(configRegistry);
			registerColumnEightCheckbox(configRegistry);
			registerColumnNineComboBox(configRegistry);
			registerColumnTenComboBox(configRegistry);
			registerColumnElevenComboBox(configRegistry);
			registerColumnTwelveComboBox(configRegistry);
			registerColumnThirteenFileDialogEditor(configRegistry);
		}

		private void registerColumnTwoTextEditor(IConfigRegistry configRegistry) {
			// register a TextCellEditor for column two that commits on key up/down
			// moves the selection after commit by enter
			configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new TextCellEditor(true, true),
					DisplayMode.NORMAL, SortHeaderPart.COLUMN_TWO_LABEL);

			// configure to open the adjacent editor after commit
			configRegistry.registerConfigAttribute(EditConfigAttributes.OPEN_ADJACENT_EDITOR, Boolean.TRUE,
					DisplayMode.EDIT, SortHeaderPart.COLUMN_TWO_LABEL);

			// configure a custom message for the multi edit dialog
			Map<String, Object> editDialogSettings = new HashMap<>();
			editDialogSettings.put(ICellEditDialog.DIALOG_MESSAGE, "Please specify the lastname in here:");

			configRegistry.registerConfigAttribute(EditConfigAttributes.EDIT_DIALOG_SETTINGS, editDialogSettings,
					DisplayMode.EDIT, SortHeaderPart.COLUMN_TWO_LABEL);
		}

		private void registerColumnThreePasswordEditor(IConfigRegistry configRegistry) {
			// register a PasswordCellEditor for column three
			configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new PasswordCellEditor(),
					DisplayMode.NORMAL, SortHeaderPart.COLUMN_THREE_LABEL);

			// configure the password editor to not support multi editing
			configRegistry.registerConfigAttribute(EditConfigAttributes.SUPPORT_MULTI_EDIT, Boolean.FALSE,
					DisplayMode.EDIT, SortHeaderPart.COLUMN_THREE_LABEL);

			// note that you should also register the corresponding
			// PasswordTextPainter
			// to ensure that the password is not rendered in clear text
			configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, new PasswordTextPainter(),
					DisplayMode.NORMAL, SortHeaderPart.COLUMN_THREE_LABEL);
		}

		private void registerColumnFourMultiLineEditor(IConfigRegistry configRegistry) {
			// configure the multi line text editor for column four
			configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new MultiLineTextCellEditor(false),
					DisplayMode.NORMAL, SortHeaderPart.COLUMN_FOUR_LABEL);

			// configure the multi line text editor to always open in a subdialog
			configRegistry.registerConfigAttribute(EditConfigAttributes.OPEN_IN_DIALOG, Boolean.TRUE, DisplayMode.EDIT,
					SortHeaderPart.COLUMN_FOUR_LABEL);

			Style cellStyle = new Style();
			cellStyle.setAttributeValue(CellStyleAttributes.HORIZONTAL_ALIGNMENT, HorizontalAlignmentEnum.LEFT);
			configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL,
					SortHeaderPart.COLUMN_FOUR_LABEL);
			configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.EDIT,
					SortHeaderPart.COLUMN_FOUR_LABEL);

			// configure custom dialog settings
			Display display = Display.getCurrent();
			Map<String, Object> editDialogSettings = new HashMap<>();
			editDialogSettings.put(ICellEditDialog.DIALOG_SHELL_TITLE, "My custom value");
			editDialogSettings.put(ICellEditDialog.DIALOG_SHELL_ICON, display.getSystemImage(SWT.ICON_WARNING));
			editDialogSettings.put(ICellEditDialog.DIALOG_SHELL_RESIZABLE, Boolean.TRUE);

			Point size = new Point(400, 300);
			editDialogSettings.put(ICellEditDialog.DIALOG_SHELL_SIZE, size);

			int screenWidth = display.getBounds().width;
			int screenHeight = display.getBounds().height;
			Point location = new Point((screenWidth / (2 * display.getMonitors().length)) - (size.x / 2),
					(screenHeight / 2) - (size.y / 2));
			editDialogSettings.put(ICellEditDialog.DIALOG_SHELL_LOCATION, location);

			// add custum message
			editDialogSettings.put(ICellEditDialog.DIALOG_MESSAGE, "Enter some free text in here:");

			configRegistry.registerConfigAttribute(EditConfigAttributes.EDIT_DIALOG_SETTINGS, editDialogSettings,
					DisplayMode.EDIT, SortHeaderPart.COLUMN_FOUR_LABEL);
		}

		private void registerColumnFiveIntegerEditor(IConfigRegistry configRegistry) {
			// register a TextCellEditor for column five that moves the selection
			// after commit
			configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new TextCellEditor(false, true),
					DisplayMode.NORMAL, SortHeaderPart.COLUMN_FIVE_LABEL);

			// configure to open the adjacent editor after commit
			configRegistry.registerConfigAttribute(EditConfigAttributes.OPEN_ADJACENT_EDITOR, Boolean.TRUE,
					DisplayMode.EDIT, SortHeaderPart.COLUMN_FIVE_LABEL);

			// configure to open always in dialog to show the tick update in normal
			// mode
			configRegistry.registerConfigAttribute(EditConfigAttributes.OPEN_IN_DIALOG, Boolean.TRUE, DisplayMode.EDIT,
					SortHeaderPart.COLUMN_FIVE_LABEL);

			// don't forget to register the Integer converter!
			configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER,
					new DefaultIntegerDisplayConverter(), DisplayMode.NORMAL, SortHeaderPart.COLUMN_FIVE_LABEL);
		}

		private void registerColumnSixDoubleEditor(IConfigRegistry configRegistry) {
			// register a TextCellEditor for column five that moves the selection
			// after commit
			configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new TextCellEditor(false, true),
					DisplayMode.NORMAL, SortHeaderPart.COLUMN_SIX_LABEL);

			// configure to open the adjacent editor after commit
			configRegistry.registerConfigAttribute(EditConfigAttributes.OPEN_ADJACENT_EDITOR, Boolean.TRUE,
					DisplayMode.EDIT, SortHeaderPart.COLUMN_SIX_LABEL);

			// configure to open always in dialog to show the tick update in normal
			// mode
			configRegistry.registerConfigAttribute(EditConfigAttributes.OPEN_IN_DIALOG, Boolean.TRUE, DisplayMode.EDIT,
					SortHeaderPart.COLUMN_SIX_LABEL);

			// configure the tick update dialog to use the adjust mode
			configRegistry.registerConfigAttribute(TickUpdateConfigAttributes.USE_ADJUST_BY, Boolean.TRUE,
					DisplayMode.EDIT, SortHeaderPart.COLUMN_SIX_LABEL);

			// don't forget to register the Double converter!
			configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER,
					new DefaultDoubleDisplayConverter(), DisplayMode.NORMAL, SortHeaderPart.COLUMN_SIX_LABEL);
		}

		/**
		 * The following will register a default CheckBoxCellEditor for the column that
		 * carries the married information.
		 * <p>
		 * To register a CheckBoxCellEditor, you need to
		 * <ol>
		 * <li>Register the editor</li>
		 * <li>Register the painter corresponding to that editor</li>
		 * <li>Register the needed converter</li>
		 * </ol>
		 *
		 * @param configRegistry
		 */
		private void registerColumnSevenCheckbox(IConfigRegistry configRegistry) {
			// register a CheckBoxCellEditor for column three
			configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new CheckBoxCellEditor(),
					DisplayMode.EDIT, SortHeaderPart.COLUMN_SEVEN_LABEL);

			// if you want to use the CheckBoxCellEditor, you should also consider
			// using the corresponding CheckBoxPainter to show the content like a
			// checkbox in your NatTable
			configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, new CheckBoxPainter(),
					DisplayMode.NORMAL, SortHeaderPart.COLUMN_SEVEN_LABEL);

			// using a CheckBoxCellEditor also needs a Boolean conversion to work
			// correctly
			configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER,
					new DefaultBooleanDisplayConverter(), DisplayMode.NORMAL, SortHeaderPart.COLUMN_SEVEN_LABEL);
		}

		/**
		 * The following will register a CheckBoxCellEditor with custom icons for the
		 * column that carries the gender information. As a Gender is not a Boolean,
		 * there need to be a special converter registered. Note that such a converter
		 * needs to create a Boolean display value and create the canonical value out of
		 * a Boolean value again.
		 * <p>
		 * To register a CheckBoxCellEditor, you need to
		 * <ol>
		 * <li>Register the editor</li>
		 * <li>Register the painter corresponding to that editor</li>
		 * <li>Register the needed converter</li>
		 * </ol>
		 *
		 * @param configRegistry
		 */
		private void registerColumnEightCheckbox(IConfigRegistry configRegistry) {
			// register a CheckBoxCellEditor for column four
			configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new CheckBoxCellEditor(),
					DisplayMode.EDIT, SortHeaderPart.COLUMN_EIGHT_LABEL);

			// if you want to use the CheckBoxCellEditor, you should also consider
			// using the corresponding CheckBoxPainter to show the content like a
			// checkbox in your NatTable
			// in this case we use different icons to show how this works
			configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER,
					new CheckBoxPainter(GUIHelper.getImage("arrow_up"), GUIHelper.getImage("arrow_down")),
					DisplayMode.NORMAL, SortHeaderPart.COLUMN_EIGHT_LABEL);

			// using a CheckBoxCellEditor also needs a Boolean conversion to work
			// correctly
			configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER, getGenderBooleanConverter(),
					DisplayMode.NORMAL, SortHeaderPart.COLUMN_EIGHT_LABEL);
		}

		/**
		 * The following will register a ComboBoxCellEditor for the column that carries
		 * the street information.
		 * <p>
		 * To register a ComboBoxCellEditor, you only need to register the editor
		 * itself. On click to the cell you want to edit, the dropdown will open.
		 * <p>
		 * If you want to indicate in the view that this cell is editable by combobox,
		 * you need to register the corresponding painter.
		 *
		 * @param configRegistry
		 */
		private void registerColumnNineComboBox(IConfigRegistry configRegistry) {
			// register a combobox editor for the street names
			ComboBoxCellEditor comboBoxCellEditor = new ComboBoxCellEditor(Arrays.asList("Stuff", "Test"));
			configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, comboBoxCellEditor,
					DisplayMode.EDIT, SortHeaderPart.COLUMN_NINE_LABEL);
		}

		/**
		 * The following will register a ComboBoxCellEditor for the column that carries
		 * the city information. The difference to the editor in column six is that the
		 * text control of the combobox is editable and the combobox shows all entries
		 * instead of a scrollbar.
		 *
		 * @param configRegistry
		 */
		private void registerColumnTenComboBox(IConfigRegistry configRegistry) {
			// register a combobox for the city names
			ComboBoxCellEditor comboBoxCellEditor = new ComboBoxCellEditor(Arrays.asList("Stuff", "Test"), -1);
			comboBoxCellEditor.setFreeEdit(true);
			configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, comboBoxCellEditor,
					DisplayMode.EDIT, SortHeaderPart.COLUMN_TEN_LABEL);

			configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, new ComboBoxPainter(),
					DisplayMode.NORMAL, SortHeaderPart.COLUMN_TEN_LABEL);
		}

		/**
		 * The following will register a ComboBoxCellEditor for the column that carries
		 * the favourite food information. This ComboBoxCellEditor will support multiple
		 * selection. It also adds a different icon for the combo in edit mode.
		 *
		 * @param configRegistry
		 */
		private void registerColumnElevenComboBox(IConfigRegistry configRegistry) {
			// register a combobox for the city names
			ComboBoxCellEditor comboBoxCellEditor = new ComboBoxCellEditor(Arrays.asList("Stuff", "Test"), -1);
			comboBoxCellEditor.setMultiselect(true);
			comboBoxCellEditor.setUseCheckbox(true);

			// change the multi selection brackets that are added to the String that
			// is shown in the editor
			comboBoxCellEditor.setMultiselectTextBracket("", "");
			// register a special converter that removes the brackets in case the
			// returned value is a Collection
			// this is necessary because editing and displaying are not directly
			// coupled to each other
			configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER,
					new DefaultDisplayConverter() {

						@Override
						public Object canonicalToDisplayValue(Object canonicalValue) {
							if (canonicalValue instanceof Collection) {
								// Collection.toString() will add [ and ] around the
								// values in the Collection
								// So by removing the leading and ending character,
								// we remove the brackets
								String result = canonicalValue.toString();
								result = result.substring(1, result.length() - 1);
								return result;
							}
							// if the value is not a Collection we simply let the
							// super class do the conversion
							// this is necessary to show single values in the
							// ComboBox correctly
							return super.canonicalToDisplayValue(canonicalValue);
						}
					}, DisplayMode.NORMAL, SortHeaderPart.COLUMN_ELEVEN_LABEL);

			comboBoxCellEditor.setIconImage(GUIHelper.getImage("plus"));
			configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, comboBoxCellEditor,
					DisplayMode.EDIT, SortHeaderPart.COLUMN_ELEVEN_LABEL);
		}

		/**
		 * The following will register a ComboBoxCellEditor for the column that carries
		 * the favourite drinks information. The difference to the editor in column
		 * eight is that the text control of the combobox is editable and the combobox
		 * shows all entries instead of a scrollbar. It also uses a different icon for
		 * rendering the combo in normal mode.
		 *
		 * @param configRegistry
		 */
		private void registerColumnTwelveComboBox(IConfigRegistry configRegistry) {
			// register a combobox for the city names
			ComboBoxCellEditor comboBoxCellEditor = new ComboBoxCellEditor(Arrays.asList("Stuff", "Test"), -1);
			comboBoxCellEditor.setFreeEdit(true);
			comboBoxCellEditor.setMultiselect(true);
			comboBoxCellEditor.setIconImage(GUIHelper.getImage("plus"));
			configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, comboBoxCellEditor,
					DisplayMode.EDIT, SortHeaderPart.COLUMN_TWELVE_LABEL);

			configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER,
					new ComboBoxPainter(GUIHelper.getImage("plus")), DisplayMode.NORMAL,
					SortHeaderPart.COLUMN_TWELVE_LABEL);
		}

		/**
		 * The following will register a {@link FileDialogCellEditor} for the filename
		 * column. This will open the default SWT FileDialog to support file selection.
		 *
		 * @param configRegistry
		 */
		private void registerColumnThirteenFileDialogEditor(IConfigRegistry configRegistry) {
			configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new FileDialogCellEditor(),
					DisplayMode.EDIT, SortHeaderPart.COLUMN_THIRTEEN_LABEL);
		}

		/**
		 * @return Returns a simple converter for the gender of a Person.
		 *         {@link Gender#MALE} will be interpreted as <code>true</code> while
		 *         {@link Gender#FEMALE} will be interpreted as <code>false</code>
		 */
		private IDisplayConverter getGenderBooleanConverter() {
			return new DisplayConverter() {

				@Override
				public Object canonicalToDisplayValue(Object canonicalValue) {
//                if (canonicalValue instanceof Gender) {
//                    return ((Gender) canonicalValue) == Gender.MALE;
//                }
					return null;
				}

				@Override
				public Object displayToCanonicalValue(Object displayValue) {
					Boolean displayBoolean = Boolean.valueOf(displayValue.toString());
					return true;
				}

			};
		}
	}

}
