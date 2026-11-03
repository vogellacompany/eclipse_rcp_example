package com.vogella.tasks.ui.parts;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;

import jakarta.annotation.PostConstruct;

public class ImageDownloadPart {

    private static final String IMAGE_URL = "https://www.vogella.com/img/logo/index_logo.png";

    private Label statusLabel;
    private Label imageLabel;
    private ProgressBar progressBar;
    private Button downloadButton;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @PostConstruct
    public void createControls(Composite parent) {
        parent.setLayout(new GridLayout(1, false));

        downloadButton = new Button(parent, SWT.PUSH);
        downloadButton.setText("Download Image");

        progressBar = new ProgressBar(parent, SWT.INDETERMINATE | SWT.HORIZONTAL);
        progressBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        progressBar.setVisible(false);

        statusLabel = new Label(parent, SWT.NONE);
        statusLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        statusLabel.setText("Status: Idle");

        imageLabel = new Label(parent, SWT.NONE);
        imageLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false));
        imageLabel.addDisposeListener(_ -> disposeImage());

        downloadButton.addListener(SWT.Selection, _ -> startDownload());
    }

    private void startDownload() {
        progressBar.setVisible(true);
        statusLabel.setText("Status: Downloading...");
        downloadButton.setEnabled(false);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(IMAGE_URL)).GET().build();
        Display display = downloadButton.getDisplay();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofByteArray())
                .thenApply(ImageDownloadPart::decode)
                .whenComplete((imageData, throwable) -> display.asyncExec(() -> {
                    if (downloadButton.isDisposed()) {
                        return;
                    }
                    progressBar.setVisible(false);
                    downloadButton.setEnabled(true);
                    if (throwable == null) {
                        statusLabel.setText("Status: Downloaded");
                        showImage(imageData);
                    } else {
                        statusLabel.setText("Status: Download failed");
                        MessageDialog.openError(downloadButton.getShell(), "Download Failed",
                                throwable.getMessage());
                    }
                }));
    }

    // ImageData needs no display, so it can be decoded outside of the UI thread
    private static ImageData decode(HttpResponse<byte[]> response) {
        if (response.statusCode() != 200) {
            throw new IllegalStateException("Server returned status code: " + response.statusCode());
        }
        try {
            return new ImageData(new ByteArrayInputStream(response.body()));
        } catch (SWTException e) {
            throw new IllegalStateException("Failed to read image: " + e.getMessage(), e);
        }
    }

    private void showImage(ImageData imageData) {
        disposeImage();
        // SWT images hold native resources and must be created in the UI thread
        imageLabel.setImage(new Image(imageLabel.getDisplay(), imageData));
        imageLabel.requestLayout();
    }

    private void disposeImage() {
        Image image = imageLabel.getImage();
        if (image != null) {
            imageLabel.setImage(null);
            image.dispose();
        }
    }

    @Focus
    public void onFocus() {
        downloadButton.setFocus();
    }
}
