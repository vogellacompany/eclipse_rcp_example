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

/**
 * Improved RailwaySVGPart
 * - Much more complex network (more elements)
 * - Stable, pre-assigned colors per element to greatly reduce color jitter
 * - Lower probability of random state changes (so colors change less often)
 * - Uses modulo-safe indexing when generating SVG
 */
public class RailwaySVGPart {

    private Canvas canvas;
    private Display display;
    private Runnable colorChanger;
    private volatile boolean running = true;
    private Random random = new Random();

    // Individual color states for different elements (stable assignment)
    private List<TrackElement> trackElements = new ArrayList<>();
    private List<SignalElement> signalElements = new ArrayList<>();
    private List<SwitchElement> switchElements = new ArrayList<>();
    private List<StationElement> stationElements = new ArrayList<>();

    // Color palettes (kept small and intentional to reduce visual noise)
    private static final String[] TRACK_COLORS = {"#00CED1", "#20B2AA", "#48D1CC", "#40E0D0"};
    private static final String[] OCCUPIED_TRACK_COLORS = {"#FF4444", "#DC143C", "#CD5C5C"};
    private static final String[] SIGNAL_GREEN_COLORS = {"#00FF00", "#32CD32"};
    private static final String[] SIGNAL_RED_COLORS = {"#FF0000", "#FF4500"};
    private static final String[] SIGNAL_YELLOW_COLORS = {"#FFD700", "#FFA500"};
    private static final String[] SWITCH_COLORS = {"#C0C0C0", "#D3D3D3"};
    private static final String[] PLATFORM_COLORS = {"#4169E1", "#1E90FF", "#4682B4"};

    @Inject
    public RailwaySVGPart() {
        initializeElements();
    }

    private void initializeElements() {
        // Increase numbers for a very complex network
        int trackCount = 80;
        int signalCount = 60;
        int switchCount = 40;
        int stationCount = 12;

        // Initialize track segments with stable colors and random initial occupied state
        for (int i = 0; i < trackCount; i++) {
            boolean occ = random.nextDouble() < 0.12; // initial occupancy small
            String base = TRACK_COLORS[i % TRACK_COLORS.length];
            String occCol = OCCUPIED_TRACK_COLORS[i % OCCUPIED_TRACK_COLORS.length];
            trackElements.add(new TrackElement(occ, base, occCol));
        }

        // Initialize signals with stable color choices per signal (color set depends on state)
        for (int i = 0; i < signalCount; i++) {
            int state = random.nextInt(3); // 0=green,1=red,2=yellow
            signalElements.add(new SignalElement(state, i));
        }

        // Initialize switches with stable colors
        for (int i = 0; i < switchCount; i++) {
            boolean left = random.nextBoolean();
            String color = SWITCH_COLORS[i % SWITCH_COLORS.length];
            switchElements.add(new SwitchElement(left, color));
        }

        // Initialize stations/platforms
        for (int i = 0; i < stationCount; i++) {
            boolean trainPresent = random.nextDouble() < 0.08;
            String color = PLATFORM_COLORS[i % PLATFORM_COLORS.length];
            stationElements.add(new StationElement(trainPresent, color));
        }
    }

    @PostConstruct
    public void createComposite(Composite parent) {
        display = parent.getDisplay();

        parent.setLayout(new GridLayout(1, false));

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

                // Randomly update some elements with small probabilities to reduce color churn
                updateRandomElements();

                display.asyncExec(() -> {
                    if (!canvas.isDisposed()) {
                        canvas.redraw();
                    }
                });

                if (running) {
                    // Slow down full-state changes: every 4s
                    display.timerExec(4000, this);
                }
            }
        };

        // Blink loop for smooth small animations (e.g. flashing signals) reduced frequency
        startBlinkLoop();

        display.timerExec(4000, colorChanger);
    }

    private void startBlinkLoop() {
        Runnable blinkRunnable = new Runnable() {
            @Override
            public void run() {
                if (!running || canvas.isDisposed()) {
                    return;
                }

                canvas.redraw();

                if (running) {
                    // redraw frequently enough for smoothness but avoid excessive color flicker
                    display.timerExec(200, this);
                }
            }
        };

        display.timerExec(200, blinkRunnable);
    }

    private void updateRandomElements() {
        // Use probabilities << 1 so colors are stable for longer periods
        for (SignalElement signal : signalElements) {
            if (random.nextDouble() < 0.06) { // 6% chance to change
                signal.state = random.nextInt(3);
                // state change does not change palette, only which of the state's colors is shown
            }
        }

        for (SwitchElement sw : switchElements) {
            if (random.nextDouble() < 0.02) { // 2% chance
                sw.isLeft = !sw.isLeft;
            }
        }

        for (StationElement station : stationElements) {
            if (random.nextDouble() < 0.03) { // 3% chance
                station.trainPresent = !station.trainPresent;
            }
        }

        // Occasionally update track occupancy, but when it changes use the pre-assigned occupied color
        for (TrackElement track : trackElements) {
            if (random.nextDouble() < 0.015) { // 1.5% chance per cycle
                track.occupied = !track.occupied;
                track.updateColorFromState();
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
            <svg width="2400" height="1000" xmlns="http://www.w3.org/2000/svg">
              <defs>
                <style>
                  .track { stroke-width: 6; fill: none; }
                  .track-thin { stroke-width: 4; fill: none; }
                  .signal { stroke-width: 2; }
                  .switch-line { stroke-width: 5; fill: none; }
                  .station-label { fill: #FFFFFF; font-family: Arial, sans-serif; font-size: 12px; font-weight: bold; }
                  .track-label { fill: #FFFFFF; font-family: Arial, sans-serif; font-size: 9px; }
                  .platform { stroke: #FFFFFF; stroke-width: 2; }
                </style>
              </defs>

              <rect width="2400" height="1000" fill="#001F3F"/>
              """);

        // Multi-layered track layout with many segments — note indices use modulo to stay safe
        int y = 80;
        for (int i = 0; i < 8; i++) {
            int y1 = y + i * 28;
            svg.append(generateTrackLine(40, y1, 2360, y1, i, "Upper Line " + (i + 1)));
        }

        // Add a dense central zone with many crossovers and switches
        int centerY = 320;
        for (int l = 0; l < 6; l++) {
            int yy = centerY + l * 30;
            svg.append(generateTrackLine(100, yy, 2300, yy, 8 + l, "Central " + (l + 1)));
        }

        // Stations spread out across the map
        svg.append(generateStation(220, 70, "Hamburg Hbf", 0));
        svg.append(generateSignal(200, 80, 0));
        svg.append(generateSignal(260, 80, 1));

        svg.append(generateStation(800, 300, "Altona", 1));
        svg.append(generateSignal(780, 300, 2));

        svg.append(generateStation(1400, 320, "Dammtor", 2));
        svg.append(generateSignal(1380, 320, 3));

        svg.append(generateStation(1800, 600, "Hauptbahnhof", 3));
        svg.append(generateSignal(1780, 600, 4));

        svg.append(generateStation(2100, 820, "Bergedorf", 4));
        svg.append(generateSignal(2080, 820, 5));

        // Dense switch area
        for (int i = 0; i < 18; i++) {
            int sx = 500 + i * 60;
            int sy = 320 + (i % 6) * 12;
            svg.append(generateSwitch(sx, sy, 5 + i, (i % 2) == 0));
        }

        // Crossovers
        for (int i = 0; i < 14; i++) {
            int x1 = 300 + i * 140;
            int y1 = 200 + (i % 6) * 20;
            int x2 = x1 + 60;
            int y2 = y1 + ((i % 3) * 40 - 40);
            svg.append(generateCurvedTrack(x1, y1, x2, y2, 20 + i));
        }

        // Siding areas
        svg.append(generateTrackLine(80, 760, 900, 760, 40, "Freight Siding A"));
        svg.append(generateTrackLine(1000, 760, 1800, 760, 41, "Freight Siding B"));

        // Additional scattered signals
        for (int i = 0; i < 24; i++) {
            int sx = 120 + i * 90;
            int sy = 120 + (i % 10) * 60;
            svg.append(generateSignal(sx, sy, 10 + i));
        }

        svg.append("</svg>");
        return svg.toString();
    }

    private String generateTrackLine(int x1, int y1, int x2, int y2, int index, String label) {
        TrackElement te = trackElements.get(index % trackElements.size());
        String color = te.currentColor;

        return String.format(
            "<line class=\"track\" x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" stroke=\"%s\"/>\n" +
            "<text x=\"%d\" y=\"%d\" class=\"track-label\">%s</text>\n",
            x1, y1, x2, y2, color, x1 + 20, y1 - 6, label
        );
    }

    private String generateCurvedTrack(int x1, int y1, int x2, int y2, int index) {
        TrackElement te = trackElements.get(index % trackElements.size());
        String color = te.currentColor;
        int cx = (x1 + x2) / 2;
        return String.format(
            "<path class=\"track-thin\" d=\"M %d %d Q %d %d %d %d\" stroke=\"%s\"/>\n",
            x1, y1, cx, (y1 + y2) / 2, x2, y2, color
        );
    }

    private String generateSignal(int x, int y, int index) {
        SignalElement signal = signalElements.get(index % signalElements.size());
        String color;
        switch (signal.state) {
            case 0:
                color = SIGNAL_GREEN_COLORS[signal.stableIndex % SIGNAL_GREEN_COLORS.length];
                break;
            case 1:
                color = SIGNAL_RED_COLORS[signal.stableIndex % SIGNAL_RED_COLORS.length];
                break;
            default:
                color = SIGNAL_YELLOW_COLORS[signal.stableIndex % SIGNAL_YELLOW_COLORS.length];
                break;
        }

        // small pulsing effect: occasionally slightly brighten — computed in paint-time but using same color set
        return String.format(
            "<rect class=\"signal\" x=\"%d\" y=\"%d\" width=\"8\" height=\"12\" fill=\"%s\" stroke=\"#FFFFFF\"/>\n",
            x, y, color
        );
    }

    private String generateSwitch(int x, int y, int index, boolean left) {
        SwitchElement sw = switchElements.get(index % switchElements.size());
        String color = sw.color;

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
        String fillColor = station.trainPresent ? "#FF8C00" : station.platformColor;

        return String.format(
            "<rect class=\"platform\" x=\"%d\" y=\"%d\" width=\"120\" height=\"12\" fill=\"%s\"/>\n" +
            "<text x=\"%d\" y=\"%d\" class=\"station-label\">%s</text>\n",
            x - 60, y, fillColor, x - 40, y - 6, name
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
        final String baseColor;
        final String occupiedColor;
        String currentColor;

        TrackElement(boolean occupied, String baseColor, String occupiedColor) {
            this.occupied = occupied;
            this.baseColor = baseColor;
            this.occupiedColor = occupiedColor;
            this.currentColor = occupied ? occupiedColor : baseColor;
        }

        void updateColorFromState() {
            this.currentColor = this.occupied ? this.occupiedColor : this.baseColor;
        }
    }

    private static class SignalElement {
        int state; // 0=green, 1=red, 2=yellow
        final int stableIndex; // used to pick one color from the state's palette deterministically

        SignalElement(int state, int stableIndex) { this.state = state; this.stableIndex = stableIndex; }
    }

    private static class SwitchElement {
        boolean isLeft;
        final String color;

        SwitchElement(boolean isLeft, String color) { this.isLeft = isLeft; this.color = color; }
    }

    private static class StationElement {
        boolean trainPresent;
        final String platformColor;

        StationElement(boolean trainPresent, String platformColor) { this.trainPresent = trainPresent; this.platformColor = platformColor; }
    }
}
