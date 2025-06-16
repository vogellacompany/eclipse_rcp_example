package com.vogella.tasks.ui.util;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableColumn;
import org.osgi.service.prefs.BackingStoreException;

public class TableColumnPersistenceUtil {
	private static final String PREF_FILE = "tablecolumns.properties";
	private static IEclipsePreferences store = InstanceScope.INSTANCE.getNode(PREF_FILE);

	public static void restoreColumnWidths(TableViewer viewer, String tableKey) {
		TableColumn[] columns = viewer.getTable().getColumns();
		for (int i = 0; i < columns.length; i++) {
			int width = store.getInt(tableKey + "_col" + i, 200);
			if (width > 0) {
				columns[i].setWidth(width);
			}
		}
	}

	public static void saveColumnWidths(TableViewer viewer, String tableKey) {
		TableColumn[] columns = viewer.getTable().getColumns();
		for (int i = 0; i < columns.length; i++) {
			store.putInt(tableKey + "_col" + i, columns[i].getWidth());
		}
		try {
			store.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
}
