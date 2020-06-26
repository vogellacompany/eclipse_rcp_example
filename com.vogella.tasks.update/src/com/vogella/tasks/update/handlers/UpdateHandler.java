package com.vogella.tasks.update.handlers;

import java.net.URI;
import java.net.URISyntaxException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.equinox.p2.core.IProvisioningAgent;

import org.eclipse.equinox.p2.operations.ProvisioningJob;

import org.eclipse.equinox.p2.operations.ProvisioningSession;

import org.eclipse.equinox.p2.operations.UpdateOperation;

public class UpdateHandler {

	private static final String REPOSITORY_LOC = System.getProperty("UpdateHandler.Repo", "http://localhost/repository");

	private IWorkbench workbench;

	@Execute
	public void execute(final IProvisioningAgent agent, IWorkbench workbench) {
		this.workbench = workbench;
		Job updateJob = Job.create("Update Job", monitor -> {
			performUpdates(agent, monitor);
		});
		updateJob.schedule();

	}

	private IStatus performUpdates(final IProvisioningAgent agent, IProgressMonitor monitor) {
		// configure update operation
		final ProvisioningSession session = new ProvisioningSession(agent);
		final UpdateOperation operation = new UpdateOperation(session);
		// create uri and check for validity

		URI uri = null;

		try {
			uri = new URI(REPOSITORY_LOC);
		} catch (URISyntaxException e) {
			throw new OperationCanceledException("Invalid repository location");

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