package com.vogella.tasks.ui.parts;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;

public class StellwerkViewerPart {

    private Canvas canvas;
    private Display display;
    private Combo colorCombo;
    private String svgFilePath;
    private String currentSvgContent;
    
    // Color options for stations
    private static final String[][] STATION_COLORS = {
        {"Blau (Standard)", "#4169E1"},
        {"Grün", "#00AA00"},
        {"Gelb", "#FFD700"},
        {"Orange", "#FF8C00"},
        {"Violett", "#8B00FF"},
        {"Türkis", "#00CED1"},
        {"Rosa", "#FF69B4"},
        {"Grau", "#808080"}
    };

    @Inject
    public StellwerkViewerPart() {
    }

    @PostConstruct
    public void createComposite(Composite parent) {
        display = parent.getDisplay();
        
        parent.setLayout(new GridLayout(1, false));
        
        // Create control composite
        Composite controlComposite = new Composite(parent, SWT.NONE);
        controlComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        controlComposite.setLayout(new GridLayout(5, false));
        
        // File selection button
        Button loadButton = new Button(controlComposite, SWT.PUSH);
        loadButton.setText("SVG laden...");
        loadButton.addListener(SWT.Selection, e -> loadSvgFile());
        
        Label colorLabel = new Label(controlComposite, SWT.NONE);
        colorLabel.setText("Bahnhof-Farbe:");
        
        // Color combo box
        colorCombo = new Combo(controlComposite, SWT.READ_ONLY);
        for (String[] color : STATION_COLORS) {
            colorCombo.add(color[0]);
        }
        colorCombo.select(0); // Default to blue
        
        // Reload button
        Button reloadButton = new Button(controlComposite, SWT.PUSH);
        reloadButton.setText("Neu laden mit Farbe");
        reloadButton.addListener(SWT.Selection, e -> reloadSvgWithColor());
        
        // Reset button
        Button resetButton = new Button(controlComposite, SWT.PUSH);
        resetButton.setText("Zurücksetzen");
        resetButton.addListener(SWT.Selection, e -> {
            colorCombo.select(0);
            reloadSvgWithColor();
        });
        
        // Canvas for SVG display
        canvas = new Canvas(parent, SWT.NONE);
        canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        canvas.setBackground(display.getSystemColor(SWT.COLOR_GRAY));
        
        canvas.addPaintListener(e -> {
            paintSVG(e.gc);
        });
        
        // Load default message
        currentSvgContent = generateWelcomeSvg();
    }
    
    private void loadSvgFile() {
        FileDialog dialog = new FileDialog(canvas.getShell(), SWT.OPEN);
        dialog.setText("SVG-Datei auswählen");
        dialog.setFilterExtensions(new String[] {"*.svg", "*.*"});
        dialog.setFilterNames(new String[] {"SVG Dateien (*.svg)", "Alle Dateien (*.*)"});
        
        String selected = dialog.open();
        if (selected != null) {
            svgFilePath = selected;
            reloadSvgWithColor();
        }
    }
    
    private void reloadSvgWithColor() {
        if (svgFilePath == null || svgFilePath.isEmpty()) {
            showError("Bitte zuerst eine SVG-Datei laden!");
            return;
        }
        
        try {
            // Read the SVG file
            String svgContent = new String(Files.readAllBytes(Paths.get(svgFilePath)), StandardCharsets.UTF_8);
            
            // Get selected color
            int selectedIndex = colorCombo.getSelectionIndex();
            if (selectedIndex < 0) {
				selectedIndex = 0;
			}
            String selectedColor = STATION_COLORS[selectedIndex][1];
            
            // Apply color filter - replace the placeholder in the SVG
            // This assumes the SVG has a placeholder like #STATION_COLOR#
            svgContent = svgContent.replace("#STATION_COLOR#", selectedColor);
            
            // Also replace any class-based station colors in the style section
            svgContent = svgContent.replaceAll(
                "(\\.station-platform\\s*\\{[^}]*fill:\\s*)#[0-9A-Fa-f]{6}",
                "$1" + selectedColor
            );
            
            currentSvgContent = svgContent;
            canvas.redraw();
            
        } catch (Exception e) {
            e.printStackTrace();
            showError("Fehler beim Laden der SVG-Datei: " + e.getMessage());
        }
    }
    
    private void paintSVG(GC gc) {
        if (currentSvgContent == null || currentSvgContent.isEmpty()) {
            gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
            gc.drawString("Keine SVG-Datei geladen", 20, 20);
            return;
        }
        
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(
                currentSvgContent.getBytes(StandardCharsets.UTF_8));
            
            ImageData imageData = new ImageData(inputStream);
            org.eclipse.swt.graphics.Image image = new org.eclipse.swt.graphics.Image(display, imageData);
            
            int canvasWidth = canvas.getClientArea().width;
            int canvasHeight = canvas.getClientArea().height;
            
            if (canvasWidth > 0 && canvasHeight > 0) {
                double scaleX = (double) canvasWidth / image.getBounds().width;
                double scaleY = (double) canvasHeight / image.getBounds().height;
                double scale = Math.min(scaleX, scaleY) * 0.95;
                
                int scaledWidth = (int) (image.getBounds().width * scale);
                int scaledHeight = (int) (image.getBounds().height * scale);
                
                int x = (canvasWidth - scaledWidth) / 2;
                int y = (canvasHeight - scaledHeight) / 2;
                
                gc.drawImage(image, 0, 0, image.getBounds().width, image.getBounds().height,
                           x, y, scaledWidth, scaledHeight);
            }
            
            image.dispose();
            inputStream.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
            gc.drawString("Fehler beim Rendern der SVG: " + e.getMessage(), 20, 20);
        }
    }
    
    private void showError(String message) {
        org.eclipse.swt.widgets.MessageBox messageBox = 
            new org.eclipse.swt.widgets.MessageBox(canvas.getShell(), SWT.ICON_ERROR | SWT.OK);
        messageBox.setText("Fehler");
        messageBox.setMessage(message);
        messageBox.open();
    }
    
    private String generateWelcomeSvg() {
        return """
            <?xml version="1.0" encoding="UTF-8"?>
            <svg width="800" height="400" xmlns="http://www.w3.org/2000/svg">
              <rect width="800" height="400" fill="#F0F0F0"/>
              <text x="400" y="150" text-anchor="middle" font-family="Arial" font-size="24" font-weight="bold" fill="#333333">
                Stellwerk-Simulator Viewer
              </text>
              <text x="400" y="200" text-anchor="middle" font-family="Arial" font-size="16" fill="#666666">
                Bitte laden Sie eine SVG-Datei mit "SVG laden..."
              </text>
              <text x="400" y="250" text-anchor="middle" font-family="Arial" font-size="14" fill="#888888">
                Wählen Sie eine Farbe und klicken Sie auf "Neu laden mit Farbe"
              </text>
              <text x="400" y="280" text-anchor="middle" font-family="Arial" font-size="14" fill="#888888">
                um die Bahnhof-Farben zu ändern
              </text>
              <rect x="300" y="320" width="200" height="40" fill="#4169E1" stroke="#000000" stroke-width="2"/>
              <text x="400" y="345" text-anchor="middle" font-family="Arial" font-size="14" font-weight="bold" fill="#FFFFFF">
                Beispiel Bahnhof
              </text>
            </svg>
            """;
    }
    
    @Focus
    public void setFocus() {
        canvas.setFocus();
    }
    
    @PreDestroy
    public void dispose() {
        // Cleanup if needed
    }
}