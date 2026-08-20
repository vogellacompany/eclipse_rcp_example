package com.vogella.tasks.update.handlers;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.eclipse.core.runtime.ICoreRunnable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.prefs.BackingStoreException;

import jakarta.inject.Named;

public class UpdateHandler {

	private static final String PLUGIN_ID = "com.vogella.tasks.update";
	private static final String PREF_REPOSITORY_LOCATION = "repository.location";
	private static final String DEFAULT_REPOSITORY_LOC =
			System.getProperty("UpdateHandler.Repo", "http://localhost/repository");

	private IWorkbench workbench;

	@Execute
	public void execute(final IProvisioningAgent agent, IWorkbench workbench,
			@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		this.workbench = workbench;

		// Load the last-used repository location from UserScope preferences
		IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(PLUGIN_ID);
		String savedLocation = prefs.get(PREF_REPOSITORY_LOCATION, DEFAULT_REPOSITORY_LOC);

		// Show dialog to let the user confirm or change the repository location
		RepositoryLocationDialog dialog = new RepositoryLocationDialog(shell, savedLocation);
		if (dialog.open() != Window.OK) {
			return;
		}

		String repositoryLocation = dialog.getLocation();

		// Persist the chosen location for next time
		prefs.put(PREF_REPOSITORY_LOCATION, repositoryLocation);
		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			// Non-critical: log and continue
			e.printStackTrace();
		}

		Job updateJob = Job.create("Update Job", (ICoreRunnable) monitor -> performUpdates(agent, repositoryLocation, monitor));
		updateJob.schedule();
	}

	private IStatus performUpdates(final IProvisioningAgent agent, String repositoryLocation,
			IProgressMonitor monitor) {
		// configure update operation
		final ProvisioningSession session = new ProvisioningSession(agent);
		final UpdateOperation operation = new UpdateOperation(session);

		// Build the URI – support both URLs and local file system paths
		URI uri;
		try {
			uri = toURI(repositoryLocation);
		} catch (URISyntaxException e) {
			throw new OperationCanceledException("Invalid repository location: " + repositoryLocation);
		}

		operation.getProvisioningContext().setArtifactRepositories(uri);
		operation.getProvisioningContext().setMetadataRepositories(uri);

		// check for updates, this causes I/O
		final IStatus status = operation.resolveModal(monitor);

		// failed to find updates (inform user and exit)
		if (status.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE) {
			return Status.CANCEL_STATUS;
		}

		// run installation
		ProvisioningJob provisioningJob = operation.getProvisioningJob(monitor);

		// updates cannot run from within Eclipse IDE!!!
		if (provisioningJob == null) {
			return Status.CANCEL_STATUS;
		}

		configureProvisioningJob(provisioningJob);
		provisioningJob.schedule();
		return Status.OK_STATUS;
	}

	/**
	 * Converts a string to a {@link URI}. If the string already contains a scheme
	 * (e.g. {@code http://} or {@code file://}) it is parsed directly; otherwise
	 * it is treated as a local file system path.
	 */
	private static URI toURI(String location) throws URISyntaxException {
		if (location.contains("://")) {
			return new URI(location);
		}
		// Local file system path
		return Paths.get(location).toUri();
	}

	private void configureProvisioningJob(ProvisioningJob provisioningJob) {
		// register a job change listener to track
		// installation progress and restart application in case of updates
		provisioningJob.addJobChangeListener(new JobChangeAdapter() {
			@Override
			public void done(IJobChangeEvent event) {
				if (event.getResult().isOK()) {
					workbench.restart();
				}
				super.done(event);
			}
		});
	}
}