package com.vogella.tasks.ui.parts;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;

public class RailwaySVGPart {

    private Canvas canvas;
    private Display display;
    private Runnable colorChanger;
    private volatile boolean running = true;
    private Random random = new Random();
    
    // Individual color states for different elements
    private List<TrackElement> trackElements = new ArrayList<>();
    private List<SignalElement> signalElements = new ArrayList<>();
    private List<SwitchElement> switchElements = new ArrayList<>();
    private List<StationElement> stationElements = new ArrayList<>();
    
    // Color palettes
    private String[] trackColors = {"#00FFFF", "#00CED1", "#20B2AA", "#48D1CC", "#40E0D0"};
    private String[] occupiedTrackColors = {"#FF4444", "#FF6B6B", "#DC143C", "#CD5C5C", "#F08080"};
    private String[] signalGreenColors = {"#00FF00", "#32CD32", "#00FA9A", "#90EE90", "#98FB98"};
    private String[] signalRedColors = {"#FF0000", "#FF4500", "#DC143C", "#CD5C5C", "#FA8072"};
    private String[] signalYellowColors = {"#FFFF00", "#FFD700", "#FFA500", "#FFFFE0", "#FFFACD"};
    private String[] switchColors = {"#FFFFFF", "#F0F0F0", "#E0E0E0", "#D3D3D3", "#C0C0C0"};
    private String[] platformColors = {"#4169E1", "#1E90FF", "#6495ED", "#4682B4", "#5F9EA0"};

    @Inject
    public RailwaySVGPart() {
        initializeElements();
    }
    
    private void initializeElements() {
        // Initialize track segments with random states
        for (int i = 0; i < 40; i++) {
            trackElements.add(new TrackElement(random.nextBoolean()));
        }
        
        // Initialize signals
        for (int i = 0; i < 25; i++) {
            signalElements.add(new SignalElement(random.nextInt(3))); // 0=green, 1=red, 2=yellow
        }
        
        // Initialize switches
        for (int i = 0; i < 15; i++) {
            switchElements.add(new SwitchElement(random.nextBoolean()));
        }
        
        // Initialize stations/platforms
        for (int i = 0; i < 8; i++) {
            stationElements.add(new StationElement(random.nextBoolean()));
        }
    }

    @PostConstruct
    public void createComposite(Composite parent) {
        display = parent.getDisplay();
        
        parent.setLayout(new GridLayout(1, false));
        
        // Create button composite
        Composite buttonComposite = new Composite(parent, SWT.NONE);
        buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        buttonComposite.setLayout(new GridLayout(5, false));
        
        // Create buttons
        org.eclipse.swt.widgets.Button trackButton = new org.eclipse.swt.widgets.Button(buttonComposite, SWT.PUSH);
        trackButton.setText("Randomize Tracks");
        trackButton.addListener(SWT.Selection, e -> {
            randomizeTrackColors();
            canvas.redraw();
        });
        
        org.eclipse.swt.widgets.Button signalButton = new org.eclipse.swt.widgets.Button(buttonComposite, SWT.PUSH);
        signalButton.setText("Randomize Signals");
        signalButton.addListener(SWT.Selection, e -> {
            randomizeSignalStates();
            canvas.redraw();
        });
        
        org.eclipse.swt.widgets.Button switchButton = new org.eclipse.swt.widgets.Button(buttonComposite, SWT.PUSH);
        switchButton.setText("Randomize Switches");
        switchButton.addListener(SWT.Selection, e -> {
            randomizeSwitches();
            canvas.redraw();
        });
        
        org.eclipse.swt.widgets.Button stationButton = new org.eclipse.swt.widgets.Button(buttonComposite, SWT.PUSH);
        stationButton.setText("Randomize Stations");
        stationButton.addListener(SWT.Selection, e -> {
            randomizeStations();
            canvas.redraw();
        });
        
        org.eclipse.swt.widgets.Button allButton = new org.eclipse.swt.widgets.Button(buttonComposite, SWT.PUSH);
        allButton.setText("Randomize All");
        allButton.addListener(SWT.Selection, e -> {
            randomizeTrackColors();
            randomizeSignalStates();
            randomizeSwitches();
            randomizeStations();
            canvas.redraw();
        });
        
        canvas = new Canvas(parent, SWT.NONE);
        canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        canvas.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
        
        canvas.addPaintListener(e -> {
            paintSVG(e.gc);
        });
        
        startColorAnimation();
    }
    
    private void startColorAnimation() {
        colorChanger = new Runnable() {
            @Override
            public void run() {
                if (!running || canvas.isDisposed()) {
                    return;
                }
                
                // Randomly update some elements
                updateRandomElements();
                
                display.asyncExec(() -> {
                    if (!canvas.isDisposed()) {
                        canvas.redraw();
                    }
                });
                
                if (running) {
                    display.timerExec(5000, this);
                }
            }
        };
        
        display.timerExec(5000, colorChanger);
    }
    
    private void randomizeTrackColors() {
        for (TrackElement track : trackElements) {
            track.occupied = random.nextBoolean();
        }
    }
    
    private void randomizeSignalStates() {
        for (SignalElement signal : signalElements) {
            signal.state = random.nextInt(3);
        }
    }
    
    private void randomizeSwitches() {
        for (SwitchElement sw : switchElements) {
            sw.isLeft = random.nextBoolean();
        }
    }
    
    private void randomizeStations() {
        for (StationElement station : stationElements) {
            station.trainPresent = random.nextBoolean();
        }
    }
    
    private void updateRandomElements() {
        // Update 30% of tracks
        for (TrackElement track : trackElements) {
            if (random.nextDouble() < 0.3) {
                track.occupied = random.nextBoolean();
            }
        }
        
        // Update 40% of signals
        for (SignalElement signal : signalElements) {
            if (random.nextDouble() < 0.4) {
                signal.state = random.nextInt(3);
            }
        }
        
        // Update 20% of switches
        for (SwitchElement sw : switchElements) {
            if (random.nextDouble() < 0.2) {
                sw.isLeft = !sw.isLeft;
            }
        }
        
        // Update 25% of stations
        for (StationElement station : stationElements) {
            if (random.nextDouble() < 0.25) {
                station.trainPresent = random.nextBoolean();
            }
        }
    }
    
    private void paintSVG(GC gc) {
        try {
            String svgContent = generateComplexRailwaySVG();
            
            ByteArrayInputStream inputStream = new ByteArrayInputStream(
                svgContent.getBytes(StandardCharsets.UTF_8));
            
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
            gc.drawString("Error rendering SVG: " + e.getMessage(), 10, 10);
        }
    }
    
    private String generateComplexRailwaySVG() {
        StringBuilder svg = new StringBuilder();
        svg.append("""
            <?xml version="1.0" encoding="UTF-8"?>
            <svg width="2000" height="600" xmlns="http://www.w3.org/2000/svg">
              <defs>
                <style>
                  .track { stroke-width: 6; fill: none; }
                  .track-thin { stroke-width: 4; fill: none; }
                  .signal { stroke-width: 2; }
                  .switch-line { stroke-width: 5; fill: none; }
                  .station-label { fill: #FFFFFF; font-family: Arial, sans-serif; font-size: 10px; font-weight: bold; }
                  .track-label { fill: #FFFFFF; font-family: Arial, sans-serif; font-size: 8px; }
                  .platform { stroke: #FFFFFF; stroke-width: 2; }
                </style>
              </defs>
              
              <rect width="2000" height="600" fill="#001F3F"/>
              
            """);
        
        // Upper track system (Track 1-4)
        int yPos = 80;
        svg.append(generateTrackLine(50, yPos, 1900, yPos, 0, "Track 1A"));
        svg.append(generateTrackLine(50, yPos + 20, 1900, yPos + 20, 1, "Track 1B"));
        
        // Station 1
        svg.append(generateStation(200, yPos - 15, "Hamburg Hbf", 0));
        svg.append(generateSignal(180, yPos - 10, 0));
        svg.append(generateSignal(220, yPos - 10, 1));
        
        // Complex junction 1
        svg.append(generateSwitch(400, yPos, 0, true));
        svg.append(generateSwitch(400, yPos + 20, 1, false));
        svg.append(generateCurvedTrack(400, yPos, 500, yPos + 80, 2));
        
        // Middle track system
        yPos = 160;
        svg.append(generateTrackLine(500, yPos, 1900, yPos, 3, "Track 2A"));
        svg.append(generateTrackLine(500, yPos + 20, 1900, yPos + 20, 4, "Track 2B"));
        svg.append(generateTrackLine(500, yPos + 40, 1900, yPos + 40, 5, "Track 2C"));
        
        // Station 2
        svg.append(generateStation(750, yPos - 15, "Altona", 1));
        svg.append(generateSignal(730, yPos - 10, 2));
        svg.append(generateSignal(770, yPos + 55, 3));
        
        // Multiple switches
        svg.append(generateSwitch(900, yPos, 2, true));
        svg.append(generateSwitch(900, yPos + 20, 3, false));
        svg.append(generateSwitch(900, yPos + 40, 4, true));
        
        // Crossover sections
        svg.append(generateCurvedTrack(900, yPos, 950, yPos + 20, 6));
        svg.append(generateCurvedTrack(900, yPos + 40, 950, yPos + 20, 7));
        
        // Station 3 with platforms
        svg.append(generateStation(1100, yPos - 15, "Dammtor", 2));
        svg.append(generateSignal(1080, yPos - 10, 4));
        svg.append(generateSignal(1120, yPos + 55, 5));
        
        // Lower track system
        yPos = 300;
        svg.append(generateTrackLine(50, yPos, 1900, yPos, 8, "Track 3A"));
        svg.append(generateTrackLine(50, yPos + 20, 1900, yPos + 20, 9, "Track 3B"));
        svg.append(generateTrackLine(50, yPos + 40, 1900, yPos + 40, 10, "Track 3C"));
        svg.append(generateTrackLine(50, yPos + 60, 1900, yPos + 60, 11, "Track 3D"));
        
        // Junction connecting upper and lower
        svg.append(generateCurvedTrack(600, 180, 700, yPos, 12));
        svg.append(generateSwitch(700, yPos, 5, true));
        
        // Station 4 - Large station
        svg.append(generateStation(1300, yPos - 15, "Hauptbahnhof", 3));
        svg.append(generateSignal(1280, yPos - 10, 6));
        svg.append(generateSignal(1320, yPos - 10, 7));
        svg.append(generateSignal(1280, yPos + 75, 8));
        svg.append(generateSignal(1320, yPos + 75, 9));
        
        // Complex switch yard
        svg.append(generateSwitch(1450, yPos, 6, false));
        svg.append(generateSwitch(1450, yPos + 20, 7, true));
        svg.append(generateSwitch(1450, yPos + 40, 8, false));
        svg.append(generateSwitch(1450, yPos + 60, 9, true));
        
        svg.append(generateCurvedTrack(1450, yPos, 1550, yPos + 20, 13));
        svg.append(generateCurvedTrack(1450, yPos + 40, 1550, yPos + 20, 14));
        
        // Station 5
        svg.append(generateStation(1650, yPos - 15, "Bergedorf", 4));
        svg.append(generateSignal(1630, yPos + 10, 10));
        svg.append(generateSignal(1670, yPos + 30, 11));
        
        // Bottom track system with sidings
        yPos = 450;
        svg.append(generateTrackLine(50, yPos, 1900, yPos, 15, "Track 4A"));
        svg.append(generateTrackLine(50, yPos + 20, 1900, yPos + 20, 16, "Track 4B"));
        
        // Depot/Siding connections
        svg.append(generateSwitch(300, yPos, 10, true));
        svg.append(generateCurvedTrack(300, yPos, 350, yPos - 50, 17));
        svg.append(generateTrackLine(350, yPos - 50, 500, yPos - 50, 18, "Siding 1"));
        
        svg.append(generateSwitch(800, yPos + 20, 11, false));
        svg.append(generateCurvedTrack(800, yPos + 20, 850, yPos + 70, 19));
        svg.append(generateTrackLine(850, yPos + 70, 1000, yPos + 70, 20, "Siding 2"));
        
        // Station 6
        svg.append(generateStation(1500, yPos - 15, "Harburg", 5));
        svg.append(generateSignal(1480, yPos - 10, 12));
        
        // More signals throughout
        svg.append(generateSignal(550, 160, 13));
        svg.append(generateSignal(1000, 180, 14));
        svg.append(generateSignal(350, 320, 15));
        svg.append(generateSignal(850, 340, 16));
        svg.append(generateSignal(1150, 360, 17));
        svg.append(generateSignal(1750, 320, 18));
        svg.append(generateSignal(600, 470, 19));
        svg.append(generateSignal(1200, 470, 20));
        
        // Additional switches
        svg.append(generateSwitch(1750, 300, 12, true));
        svg.append(generateSwitch(1750, 320, 13, false));
        svg.append(generateSwitch(250, 450, 14, true));
        
        svg.append("</svg>");
        return svg.toString();
    }
    
    private String generateTrackLine(int x1, int y1, int x2, int y2, int index, String label) {
        String color = trackElements.get(index % trackElements.size()).occupied ? 
            occupiedTrackColors[random.nextInt(occupiedTrackColors.length)] :
            trackColors[random.nextInt(trackColors.length)];
        
        return String.format(
            "<line class=\"track\" x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" stroke=\"%s\"/>\n" +
            "<text x=\"%d\" y=\"%d\" class=\"track-label\">%s</text>\n",
            x1, y1, x2, y2, color, x1 + 20, y1 - 5, label
        );
    }
    
    private String generateCurvedTrack(int x1, int y1, int x2, int y2, int index) {
        String color = trackElements.get(index % trackElements.size()).occupied ? 
            occupiedTrackColors[random.nextInt(occupiedTrackColors.length)] :
            trackColors[random.nextInt(trackColors.length)];
        
        int cx = (x1 + x2) / 2;
        return String.format(
            "<path class=\"track-thin\" d=\"M %d %d Q %d %d %d %d\" stroke=\"%s\"/>\n",
            x1, y1, cx, (y1 + y2) / 2, x2, y2, color
        );
    }
    
    private String generateSignal(int x, int y, int index) {
        SignalElement signal = signalElements.get(index % signalElements.size());
        String color;
        if (signal.state == 0) {
            color = signalGreenColors[random.nextInt(signalGreenColors.length)];
        } else if (signal.state == 1) {
            color = signalRedColors[random.nextInt(signalRedColors.length)];
        } else {
            color = signalYellowColors[random.nextInt(signalYellowColors.length)];
        }
        
        return String.format(
            "<rect class=\"signal\" x=\"%d\" y=\"%d\" width=\"8\" height=\"12\" fill=\"%s\" stroke=\"#FFFFFF\"/>\n",
            x, y, color
        );
    }
    
    private String generateSwitch(int x, int y, int index, boolean left) {
        SwitchElement sw = switchElements.get(index % switchElements.size());
        String color = switchColors[random.nextInt(switchColors.length)];
        
        if (sw.isLeft) {
            return String.format(
                "<line class=\"switch-line\" x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" stroke=\"%s\"/>\n",
                x, y, x + 30, y - 15, color
            );
        } else {
            return String.format(
                "<line class=\"switch-line\" x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" stroke=\"%s\"/>\n",
                x, y, x + 30, y + 15, color
            );
        }
    }
    
    private String generateStation(int x, int y, String name, int index) {
        StationElement station = stationElements.get(index % stationElements.size());
        String color = platformColors[random.nextInt(platformColors.length)];
        String fillColor = station.trainPresent ? "#FF8C00" : color;
        
        return String.format(
            "<rect class=\"platform\" x=\"%d\" y=\"%d\" width=\"80\" height=\"10\" fill=\"%s\"/>\n" +
            "<text x=\"%d\" y=\"%d\" class=\"station-label\">%s</text>\n",
            x - 40, y, fillColor, x, y - 5, name
        );
    }
    
    @Focus
    public void setFocus() {
        canvas.setFocus();
    }
    
    @PreDestroy
    public void dispose() {
        running = false;
    }
    
    // Element classes
    private static class TrackElement {
        boolean occupied;
        TrackElement(boolean occupied) { this.occupied = occupied; }
    }
    
    private static class SignalElement {
        int state; // 0=green, 1=red, 2=yellow
        SignalElement(int state) { this.state = state; }
    }
    
    private static class SwitchElement {
        boolean isLeft;
        SwitchElement(boolean isLeft) { this.isLeft = isLeft; }
    }
    
    private static class StationElement {
        boolean trainPresent;
        StationElement(boolean trainPresent) { this.trainPresent = trainPresent; }
    }
}