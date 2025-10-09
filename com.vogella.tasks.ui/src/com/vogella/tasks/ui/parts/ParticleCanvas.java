package com.vogella.tasks.ui.parts;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import jakarta.annotation.PostConstruct;

public class ParticleCanvas {
	
    private static class Particle {
        double x, y, vx, vy;
        int size;
        Color color;
        
        Particle(int w, int h, Display d) {
            x = Math.random() * w;
            y = Math.random() * h;
            vx = (Math.random() - 0.5) * 3;
            vy = (Math.random() - 0.5) * 3;
            size = (int)(Math.random() * 8 + 3);
            int r = (int)(Math.random() * 100 + 155);
            int g = (int)(Math.random() * 100 + 100);
            int b = (int)(Math.random() * 155 + 100);
            color = new Color(d, r, g, b);
        }
        
        void update(int w, int h) {
            x += vx;
            y += vy;
            if (x < 0 || x > w) {
				vx *= -1;
			}
            if (y < 0 || y > h) {
				vy *= -1;
			}
            x = Math.max(0, Math.min(w, x));
            y = Math.max(0, Math.min(h, y));
        }
    }
    @PostConstruct
    public void createUserInterface(Composite parent) {
	
    
        Canvas canvas = new Canvas(parent, SWT.DOUBLE_BUFFERED);
        List<Particle> particles = new ArrayList<>();
        
        canvas.addListener(SWT.Resize, e -> {
            particles.clear();
            Rectangle r = canvas.getBounds();
            for (int i = 0; i < 80; i++) {
                particles.add(new Particle(r.width, r.height, parent.getDisplay()));
            }
        });
        
        canvas.addPaintListener(e -> {
            GC gc = e.gc;
            gc.setAntialias(SWT.ON);
            Rectangle r = canvas.getBounds();
            
            // Gradient background
            gc.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_BLACK));
            gc.setBackground(new Color(parent.getDisplay(), 20, 20, 40));
            gc.fillGradientRectangle(0, 0, r.width, r.height, true);
            
            // Draw connections
            gc.setAlpha(30);
            gc.setForeground(new Color(parent.getDisplay(), 100, 150, 255));
            for (int i = 0; i < particles.size(); i++) {
                for (int j = i + 1; j < particles.size(); j++) {
                    Particle p1 = particles.get(i);
                    Particle p2 = particles.get(j);
                    double dist = Math.hypot(p1.x - p2.x, p1.y - p2.y);
                    if (dist < 100) {
                        gc.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
                    }
                }
            }
            
            // Draw particles
            gc.setAlpha(200);
            for (Particle p : particles) {
                gc.setBackground(p.color);
                gc.fillOval((int)p.x - p.size/2, (int)p.y - p.size/2, p.size, p.size);
            }
        });
        
        // Animation loop
        parent.getDisplay().timerExec(30, new Runnable() {
            @Override
			public void run() {
                if (!canvas.isDisposed()) {
                    Rectangle r = canvas.getBounds();
                    for (Particle p : particles) {
                        p.update(r.width, r.height);
                    }
                    canvas.redraw();
                    parent.getDisplay().timerExec(30, this);
                }
            }
        });
        
    }
}