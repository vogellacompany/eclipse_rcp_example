package com.vogella.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class ImageWithText extends Canvas {

    private Image image;

	public ImageWithText(Composite parent, int style) {
		super(parent, style);
		Display display = parent.getDisplay();
        image = new Image(parent.getDisplay(), ImageWithText.class.getResourceAsStream("/image/vogella.svg"));

        // Add PaintListener to draw image and text
        this.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                Rectangle bounds = image.getBounds();
                e.gc.drawImage(image, 0, 0);

                // Set text properties
                e.gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
                e.gc.setFont(display.getSystemFont());

                String text = "Advanced Trainings";
                Point textSize = e.gc.textExtent(text);

                // Draw text centered at the top of the image
                int x = (bounds.width - textSize.x) / 2;
                int y = 20;

                e.gc.drawText(text, x, y, true);
            }
        });

      
    }
	@Override
	public void dispose() {
		image.dispose();
		super.dispose();
	}
}
