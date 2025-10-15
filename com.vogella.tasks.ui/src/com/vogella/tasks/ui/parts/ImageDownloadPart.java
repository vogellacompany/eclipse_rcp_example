package com.vogella.tasks.ui.parts;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.jface.dialogs.MessageDialog;
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
    private Shell shell;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @PostConstruct
    public void createControls(Composite parent) {
        this.shell = parent.getShell();
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
            String imageUrl = "https://www.vogella.com/img/logo/index_logo.png";
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
                    String errorMessage = null;
                    
                    // Handle exceptions during HTTP request
                    if (throwable != null) {
                        errorMessage = buildErrorMessage("Network Error", 
                            "Failed to connect to server: " + throwable.getMessage(), 
                            imageUrl);
                        logError("HTTP request failed", throwable);
                    } 
                    // Handle HTTP error status codes
                    else if (response.statusCode() != 200) {
                        errorMessage = buildErrorMessage("HTTP Error", 
                            "Server returned status code: " + response.statusCode(), 
                            imageUrl);
                        logError("HTTP status error: " + response.statusCode(), null);
                    } 
                    // Handle file I/O errors
                    else {
                        try (FileOutputStream fos = new FileOutputStream(savePath)) {
                            fos.write(response.body());
                            success = true;
                        } catch (IOException e) {
                            errorMessage = buildErrorMessage("File Save Error", 
                                "Failed to save file: " + e.getMessage(), 
                                savePath);
                            logError("File write failed", e);
                        }
                    }
                    
                    final boolean finalSuccess = success;
                    final String finalErrorMessage = errorMessage;
                    
                    Display.getDefault().asyncExec(() -> {
                        if (downloadButton == null || downloadButton.isDisposed()) {
                            return;
                        }

                        progressBar.setVisible(false);
                        downloadButton.setEnabled(true);
                        
                        if (finalSuccess) {
                            statusLabel.setText("Status: Downloaded and saved successfully");
                        } else {
                            statusLabel.setText("Status: Download failed");
                            // Show error dialog with details
                            showErrorDialog("Download Failed", finalErrorMessage);
                        }
                    });
                });
    }

    private String buildErrorMessage(String errorType, String details, String context) {
        StringBuilder sb = new StringBuilder();
        sb.append(errorType).append("\n\n");
        sb.append("Details: ").append(details).append("\n");
        sb.append("Context: ").append(context);
        return sb.toString();
    }

    private void showErrorDialog(String title, String message) {
        if (shell != null && !shell.isDisposed()) {
            MessageDialog.openError(shell, title, message);
        }
    }

    private void logError(String message, Throwable throwable) {
        System.err.println("ImageDownloadPart Error: " + message);
        if (throwable != null) {
            throwable.printStackTrace();
        }
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