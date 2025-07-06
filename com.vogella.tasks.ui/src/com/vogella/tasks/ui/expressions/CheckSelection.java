package com.vogella.tasks.ui.expressions;

import org.eclipse.e4.core.di.annotations.Evaluate;
import org.eclipse.e4.core.di.annotations.Optional;

import com.vogella.tasks.model.Task;

import jakarta.inject.Named;

import java.util.Collection;

public class CheckSelection {
	@Evaluate
	public boolean evaluate(@Optional @Named("org.eclipse.ui.selection") Object o) {
		if (o instanceof Task) {
			return true;
		}
		if (o instanceof Collection<?>) {
			for (Object item : (Collection<?>) o) {
				if (item instanceof Task) {
					return true;
				}
			}
		}
		return false;
	}
}
