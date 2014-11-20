package com.google.coconnell84;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * A class that constructs a frame that can show a {@link BufferedImage} and any
 * overlays, as defined as 2D points
 * 
 */
public class ImageWindow {

    /**
     * Factory image to construct an Image Window
     * @param anImage
     * 	The initial image
     * @param pTitle
     * 	The title to display on the title
     * @return
     * 	A fully constructed {@link ImageWindow} that creates a {@link JFrame} with
     * the {@link BufferedImage} to be drawn. 
     */
    public static ImageWindow createImageFrame(final Image anImage,
	    final String pTitle) {
	final ImageWindow aWindow = new ImageWindow(anImage, pTitle);

	return aWindow;
    }

    /**
     * The JFRAME to show
     */
    private final JFrame aFrame = new JFrame();

    /**
     * The image to dispay. If this image is null, nothing is drawn
     */
    private Image mImage;

    /**
     * List over overlay points to draw
     */
    private List<Point2D> mOverlays;
    
    /**
     * The {@link JPanel} that draws the image
     */
    private final JPanel mView = new JPanel() {
	@Override
	protected void paintComponent(final java.awt.Graphics g) {
	    final Graphics2D g2 = (Graphics2D) g;
	    super.paintComponent(g2);
	    if (mImage != null) {
		g2.drawImage(mImage, 0, 0, mImage.getWidth(null),
			mImage.getHeight(null), null);

		if (mOverlays != null) {
		    final Color oldColor = g.getColor();
		    g2.setColor(Color.RED);
		    for (final Point2D aFeature : mOverlays) {
			g2.drawOval((int) aFeature.getX() - 5,
				(int) aFeature.getY() - 5, 5, 5);
		    }
		    g2.setColor(oldColor);
		}
	    }
	};
    };

    private ImageWindow(final Image pImage, final String pTitle) {
	mImage = pImage;
	aFrame.setTitle(pTitle);
	if (pImage != null) {
	    mView.setPreferredSize(new Dimension(mImage.getWidth(null), mImage
		    .getHeight(null)));
	}

	aFrame.setContentPane(mView);
	aFrame.pack();
	aFrame.setVisible(true);
    }

    /**
     * Method to set the overlay points. Once this is set, the panel will be redrawn
     * @param pOverlays
     * 	List of overlay points to display
     */
    public void setOverlays(final List<Point2D> pOverlays) {
	mOverlays = pOverlays;
	mView.repaint();

    }

    /**
     * Method to update the drawn image
     * @param pImage
     * 	The new image to draw. If the image isn't null, the underlying JFrame will
     * be resized to fit the image
     */
    public void updateImage(final BufferedImage pImage) {

	if (SwingUtilities.isEventDispatchThread()) {
	    mImage = pImage;

	    if (mImage != null) {
		mView.setPreferredSize(new Dimension(mImage.getWidth(null),
			mImage.getHeight(null)));
		aFrame.pack();
	    }
	    mView.repaint();
	} else {
	    SwingUtilities.invokeLater(new Runnable() {

		@Override
		public void run() {
		    updateImage(pImage);

		}
	    });
	}
    }

}
