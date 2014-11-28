package com.google.coconnell84.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JPanel;

public class ImageRenderer {
    
    /**
     * The image to draw as the background
     */
    private Image mImage;
    
    /**
     * List off overlays to draw on top of the image
     */
    private final List<IRenderable>mRenderables = new CopyOnWriteArrayList<>();

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

		for(IRenderable aRenderable : mRenderables) {
		    aRenderable.render(g2);
		}
//		if (mOverlays != null) {
//		    final Color oldColor = g.getColor();
//		    g2.setColor(Color.RED);
//		    for (final Point2D aFeature : mOverlays) {
//			g2.drawOval((int) aFeature.getX() - 5,
//				(int) aFeature.getY() - 5, 5, 5);
//		    }
//		    g2.setColor(oldColor);
//		}
	    }
	};
    };
    
    public void setImage(Image pImage) {
	mImage = pImage;
	if (pImage != null) {
	    mView.setPreferredSize(new Dimension(mImage.getWidth(null), mImage
		    .getHeight(null)));
	}
    }
    public Image getImage() {
	return mImage;
    }
    public JPanel getView() {
	return mView;
    }
    public void addRenderable(IRenderable pIRenderable) {
	mRenderables.add(pIRenderable);
	mView.repaint();
	
    }

}
