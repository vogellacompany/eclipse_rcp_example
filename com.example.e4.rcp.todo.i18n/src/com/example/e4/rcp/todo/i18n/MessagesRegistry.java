package com.example.e4.rcp.todo.i18n;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.nls.BaseMessageRegistry;
import org.eclipse.e4.core.services.nls.Translation;

@Creatable
public class MessagesRegistry extends BaseMessageRegistry<Messages> {

	@Inject
	@Override
	public void updateMessages(@Translation Messages messages) {
		// Update the Messages for the BaseMessageRegistry by DI
		super.updateMessages(messages);
	}
}
