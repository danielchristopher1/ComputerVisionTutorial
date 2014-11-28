package com.google.coconnell84;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.google.coconnell84.utils.IRenderable;
import com.google.coconnell84.utils.ImageRenderer;

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
    
    private final ImageRenderer mView = new ImageRenderer();

    private PointRenderer mRenderable;


    private ImageWindow(final Image pImage, final String pTitle) {
	mView.setImage(pImage);
	mRenderable = new PointRenderer();
	mView.addRenderable(mRenderable);
	aFrame.setTitle(pTitle);
	aFrame.setContentPane(mView.getView());
	aFrame.pack();
	aFrame.setVisible(true);
    }

    /**
     * Method to set the overlay points. Once this is set, the panel will be redrawn
     * @param pOverlays
     * 	List of overlay points to display
     */
    public void setOverlays(final List<Point2D> pOverlays) {
	mRenderable.setPoints(pOverlays);
	mView.getView().repaint();

    }

    /**
     * Method to update the drawn image
     * @param pImage
     * 	The new image to draw. If the image isn't null, the underlying JFrame will
     * be resized to fit the image
     */
    public void updateImage(final BufferedImage pImage) {

	if (SwingUtilities.isEventDispatchThread()) {
	    mView.setImage(pImage);
	    aFrame.pack();
	    
	} else {
	    SwingUtilities.invokeLater(new Runnable() {

		@Override
		public void run() {
		    updateImage(pImage);

		}
	    });
	}
    }
    
    private class PointRenderer implements IRenderable {
	private List<Point2D>mPoints = null;
	
	public void setPoints(List<Point2D>pPoints) {
	    mPoints = pPoints;
	}
	@Override
	public void render(Graphics2D pGraphics) {
	    final Color oldColor = pGraphics.getColor();
	    pGraphics.setColor(Color.RED);

	    if(mPoints != null) {
		for (final Point2D aFeature : mPoints) {
		    pGraphics.drawOval((int) aFeature.getX() - 5,
			    (int) aFeature.getY() - 5, 5, 5);
		}
	    }
	    pGraphics.setColor(oldColor);

	}
    }

}
