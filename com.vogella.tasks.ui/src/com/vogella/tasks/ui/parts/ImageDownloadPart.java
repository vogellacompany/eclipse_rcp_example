package com.vogella.tasks.ui.parts;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import jakarta.annotation.PostConstruct;

public class ImageDownloadPart {

    private Label statusLabel;
    private ProgressBar progressBar;
    private Button downloadButton;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @PostConstruct
    public void createControls(Composite parent) {
        parent.setLayout(new GridLayout(1, false));

        downloadButton = new Button(parent, SWT.PUSH);
        downloadButton.setText("Download and Save Image");

        progressBar = new ProgressBar(parent, SWT.INDETERMINATE | SWT.HORIZONTAL);
        progressBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        progressBar.setVisible(false);

        statusLabel = new Label(parent, SWT.NONE);
        statusLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        statusLabel.setText("Status: Idle");

        downloadButton.addListener(SWT.Selection, e -> {
            String imageUrl = "https://upload.wikimedia.org/wikipedia/commons/4/47/PNG_transparency_demonstration_1.png";
            String savePath = promptForSavePath(parent.getShell());
            if (savePath != null) {
                startDownload(imageUrl, savePath);
            }
        });
    }

    private String promptForSavePath(Shell shell) {
        FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
        fileDialog.setText("Save Image As");
        fileDialog.setFilterExtensions(new String[]{"*.png", "*.*"});
        fileDialog.setFileName("downloaded_image.png");
        return fileDialog.open();
    }

    private void startDownload(String imageUrl, String savePath) {
        Display.getDefault().asyncExec(() -> {
        	if (downloadButton == null || downloadButton.isDisposed()) {
                return;
            }
            progressBar.setVisible(true);
            statusLabel.setText("Status: Downloading...");
            downloadButton.setEnabled(false);
        });

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(imageUrl))
                .GET()
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofByteArray())
                .whenComplete((response, throwable) -> {
                    boolean success = false;
                    if (throwable == null && response.statusCode() == 200) {
                        try (FileOutputStream fos = new FileOutputStream(savePath)) {
                            fos.write(response.body());
                            success = true;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    final boolean finalSuccess = success;
                    Display.getDefault().asyncExec(() -> {
                        if (downloadButton == null || downloadButton.isDisposed()) {
                            return;
                        }

                        progressBar.setVisible(false);
                        downloadButton.setEnabled(true);
                        statusLabel.setText(finalSuccess
                                ? "Status: Downloaded and saved successfully"
                                : "Status: Download failed");
                    });
                });
    }

    @Focus
    public void onFocus() {
        if (downloadButton != null && !downloadButton.isDisposed()) {
            downloadButton.setFocus();
        }
    }

    @Persist
    public void onSave() {
        // Optional save logic
    }
}
