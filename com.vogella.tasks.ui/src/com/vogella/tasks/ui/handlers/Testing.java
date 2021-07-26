
package com.vogella.tasks.ui.handlers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

import com.vogella.tasks.model.TaskService;

public class Testing {
	static int i = 1;

	@Execute
	public void execute(MApplication app, EModelService modelService, TaskService taskService, UISynchronize sync) {
//		List<MPart> findElements = modelService.findElements(app, null, MPart.class, null);
//		for (MPart mPart : findElements) {
//			mPart.setLabel(mPart.getLabel() + i++);
//		}
//		List<MWindow> windows = modelService.findElements(app, null, MWindow.class, null);
//		windows.get(0).setHeight(100);
//		List<MPartStack> stacks = modelService.findElements(app, "idofthestack", MPartStack.class, null);
//		MPart part = modelService.createModelElement(MPart.class);
//		part.setLabel("Dynamic");
//		part.setContributionURI("bundleclass://com.vogella.tasks.ui/com.vogella.tasks.ui.parts.TodoOverviewPart");
//		stacks.get(0).getChildren().add(part);

		var client = HttpClient.newBuilder().build();
		var request = HttpRequest.newBuilder().uri(URI.create("http://www.vogella.com/")).build();
		CompletableFuture<String> thenApplyAsync = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
				.thenApplyAsync((resp) -> {
					// assume I do an UI update here
					sync.asyncExec(() -> {

					});
					
					int status = resp.statusCode();
					if (status != 200) {
						System.err.println("Error: " + resp.statusCode());
						return "NOT VALID";
					}
					return resp.body();
				});
		thenApplyAsync.join(); // prevents main() from exiting too early
	}

}